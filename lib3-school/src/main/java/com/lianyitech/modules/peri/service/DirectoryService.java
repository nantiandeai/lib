package com.lianyitech.modules.peri.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.DoubleUtils;
import com.lianyitech.core.enmu.EnumMarcPeriInfo;
import com.lianyitech.core.enmu.EnumMarcPeriSubfileInfo;
import com.lianyitech.core.marc.ConvertRecord;
import com.lianyitech.core.marc.PeriDirMarc;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.catalog.utils.SolrPage;
import com.lianyitech.modules.peri.dao.DirectoryDao;
import com.lianyitech.modules.peri.entity.Directory;
import com.lianyitech.modules.report.entity.CirculateStatistics;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/*
 *
 * Created by chenxiaoding on 20170310.
 */
@Service

public class DirectoryService extends CrudService<DirectoryDao, Directory> {

    @Autowired
    private DirectoryDao directoryDao;
    @Autowired
    @Qualifier("periSolrServer")
    private HttpSolrServer httpSolrServer;

    //保存简单书目
    @Transactional
    public String saveSimpleBook(Directory directory) {
        directory.setOrgId(UserUtils.getOrgId());//设置机构id
        int count = this.getCountByCon(directory);
        if (count > 0) {
            return "此条期刊已重复";
        }
        save(directory);//这里只修改其简单编目信息(此时如果里面存在马克数据的情况，不会修改里面属性内容，修改其属性内容在下面)
        Directory b = super.get(directory.getId());//再到数据库里面查询一遍
        String marcData = convertMarcData(b, null);//根据数据库里面存在的数据重新去生成对应的马克格式数据
        directory.setMarc64(marcData);
        directoryDao.updateMarc(directory);//再修改马克字段信息
        return null;
    }

    private Integer getCountByCon(Directory directory) {
        return directoryDao.getCountByCon(directory);
    }

    /*
	* 公用方法通过request为null区分
	* (request不为空将前端传过来的参数进行转成对应的马克数据字符串)--(request为空根据简单编目信息转换成需要的马克数据内容（取已有马克数据和模板并集）公用方法)
	* */
    private String convertMarcData(Directory directory,HttpServletRequest request){
        Record record;
        if (request != null) {
            record = ConvertRecord.makeRecordfromRequest(directory,request);
        } else {
            record = ConvertRecord.makeRecordfromBookDirectory(directory);//根据简单编目实体类将其对应的马克数据也要修改返回record
        }
        return  ConvertRecord.commonConvertMarcData(record);
    }

    /**
     *查询本馆分页期刊、solr分页书目（2种结果并合显示并分页）-如果传入了id的情况下只需要显示本馆的(这个是针对修改返回列表的情况下)
     **/
    public SolrPage findSolrBookPage(SolrPage solrpage, Page<Directory> page, Directory directory) {
        //第一步查询本馆书目带分页
        directory.setOrgId(UserUtils.getOrgId());//赋值org_id
        directory.setOrgId(UserUtils.getOrgId());//赋值org_id
        page = super.findPage(page, directory);//查询本馆带分页接口
        List<Object> list_all = new ArrayList<>();//定义一个list放所有的查询结果（本馆和solr书目(solr查询本身带从哪条到哪哪条)）
        if(page.getPageNo()==solrpage.getPageNo()){//page查询如果页数不存在会默认为1
            list_all.addAll(page.getList());
        }else{
            page.setList(new ArrayList<>());//设置为空
        }
        //这里判断是否传入了id则只需要查询单馆书目
        if(!(directory.getId()!=null && !"".equals(directory.getId()))){
            int other_start = 0;//solr查询其他馆书目起始数
            int other_rows = 0;//solr查询其他馆书目条数(由于solr查询是从起始条数 ，多少条进行查询的)
            int start = (solrpage.getPageNo() - 1) * solrpage.getPageSize();
            if(list_all.size()<solrpage.getPageSize()){//如果本馆查询的条数小于page页每页条数的话剩下的要将其他馆的书目填充到当前页
                other_rows = solrpage.getPageSize() - list_all.size();
                if(start-(int)page.getCount()>0) {
                    other_start = start-(int)page.getCount();
                }
            }
            //上面代码算剩下条以及开始行等-----------
            //查询其他馆
            String other_con_str = "!orgId:"+directory.getOrgId();//其他学校的书目信息查询条件
            String filter_con_str = "";
            if(directory.getKeyWord()!=null && !"".equals(directory.getKeyWord())){//拼凑组合查询

                String keyWords = StringEscapeUtils.unescapeHtml4(directory.getKeyWord());//解决特殊字符查询不出结果的问题
                filter_con_str = "(issn:"+keyWords+" OR title:"+keyWords+ " OR periNum:"+keyWords+" OR publishingName:"+keyWords+") ";
            }
            SolrDocumentList other_results = queryCommonSolr(other_start,other_rows,other_con_str,filter_con_str);
            for (SolrDocument other_result : other_results) {
                if( other_result.get("price")!=null ) {
                    Double p = Double.valueOf(other_result.get("price").toString());
                    other_result.setField("price",DoubleUtils.formatDoubleToString(p));
                }
            }
            list_all.addAll(other_results); // ourresuts.addAll(otherresuts);
            solrpage.setCount((int)page.getCount()+other_results.getNumFound());//设置总条数
        }else{
            solrpage.setCount((int)page.getCount());//设置总条数
        }
        solrpage.setList(list_all);//设置并合的list集合
        solrpage.initialize();//初始化算出pageCount
        return solrpage;//返回并合之后结果page
    }

    //保存对应的马克数据以及对应的期刊信息
    @Transactional
    public String savePeriMarc(Directory directory, HttpServletRequest request) {
        directory.setOrgId(UserUtils.getOrgId());
        if( directory.getId()!=null && !directory.getId().equals("")){
            directory = super.get(directory.getId());
        }
        if(directory == null){
            directory = new Directory();
        }
        String marcdata = convertMarcData(directory,request);
        int count = this.getCountByCon(directory);
        if (count > 0) {
            return "此条书目已重复";
        }
        save(directory);
        directory.setMarc64(marcdata);
        directory.setUpdateDate(new Date());
        directoryDao.updateMarc(directory);//再修改马克字段信息
        return null;
    }

    //根据条件通用查询
    private SolrDocumentList queryCommonSolr(int start,int rows,String constr,String filterstr){
        SolrQuery solrquery = new SolrQuery();
        solrquery.setQuery(constr);
        if(filterstr!=null && !("".equals(filterstr))){
            solrquery.addFilterQuery(filterstr);
        }
        solrquery.setStart(start);
        solrquery.setRows(rows);
        QueryResponse rsp;
        try {
            rsp = httpSolrServer.query(solrquery);
            return rsp.getResults();
        } catch (Exception e) {
           logger.error("操作失败",e);
            return new SolrDocumentList();
        }
    }

    //根据条件得到马克数据信息
    public Object getMarcInfo(Directory directory){
        Directory b = super.get(directory.getId());
        if (b == null) {
            b = directory;
        }
        String marcinfo = "";
        if (b.getMarc64() != null) {
            marcinfo = b.getMarc64();
        }
        InputStream inputS = null;
        try {
            inputS = new ByteArrayInputStream(marcinfo.getBytes("GBK"));
            PeriDirMarc periDirMarc = new PeriDirMarc();
            return periDirMarc.parseMarcLine(inputS,b);
        }catch (IOException e){
            logger.error("操作失败",e);
            //抛出异常的情况下另外解析
            return null;
        } finally {
            IOUtils.closeQuietly(inputS);
        }
    }

    /**
     * 看此期刊下面有无订购信息记到
     * @param periId
     * @return
     */
    public int findCountByOrder(String periId){
        String orgId = UserUtils.getOrgId();
        return dao.findCountByOrder(periId, orgId);
    }

    /**
     * 看此期刊下面有无过刊复本信息
     * @param periId
     * @return
     */
    public int findCountByBinding(String periId){
        String orgId = UserUtils.getOrgId();
        return dao.findCountByBinding(periId, orgId);
    }

    //根据tag后面模糊搜索马克主字段
    public List<Map> getMarcinfo(String tag) {
        if(tag == null ){
            tag = "";
        }
        return EnumMarcPeriInfo.getMarcByLikeTag(tag);
    }

    //马克格式验证
    private String checkMarc(List<Map> list){
        if (list != null && list.size() > 0) {
            Map<String, Map<String,Object>> tempmarc =  EnumMarcPeriInfo.getTempmarc();
            for(Map map : list){
                String tag = map.get("tag").toString();
                //String indx1 = map.get("indx1").toString();
                String mdata = map.get("mdata").toString();
                Map marcmap = EnumMarcPeriInfo.getMainNotSubFiled(tag);//得到所有的马克字段不包括子字段的
                Map<String, Object> mapinfo =  EnumMarcPeriInfo.getMainSubFiled(tag);
                Map suballfieldinfo = (Map)mapinfo.get("subfiled");
                if (marcmap!=null && marcmap.containsKey("mainfiled")) {

                    Map tempmap = (Map) tempmarc.get(tag);
                    Map tempsubfiledmap = new HashMap();//模板子字段
                    if (tempmap != null && tempmap.get("subfiled") != null) {
                        tempsubfiledmap.putAll((Map) tempmap.get("subfiled"));
                    }
                    EnumMarcPeriInfo mainfiled = (EnumMarcPeriInfo) marcmap.get("mainfiled");
                    String require = mainfiled.getPerNull();
                    String name = mainfiled.getName();
                    String ftype = mainfiled.getFtype();
                    //得到主字段信息是否是必填项
                    if (require != null && require.equals("1")) {
                        if (mdata.trim().equals("")) {
                            return tag+"("+name+")马克字段为必填项！";
                        }
                    }
                    if (ftype.equals("3")) {//数据字段区--主要是验证这块(第一判断子字段是否存在重复，第二判断是否包含了模板里面的字段信息第三判断是否必填项的内容为空)
                        int index_s = mdata.indexOf("$");
                        if ((index_s != 0 && require!=null && require.equals("1"))||(index_s==-1 && !mdata.equals(""))) {//说明第一项不是正确的马克字段（如果是必填项的情况下）
                            return tag+"("+name+")马克子字段格式有错误，第一个字母不是$";
                        } else {
                            Map<String,String> sfcfmap = new HashMap<>();//用来判断是否重复子字段
                            String[] substrs = mdata.split("\\$");
                            if ( substrs.length > 0) {
                                for (String substr : substrs) {
                                    if (substr != null && substr.length()>0) {
                                        String code = substr.substring(0, 1);
                                        String data = substr.substring(1);
                                        String tag$code = tag+"$"+code;
                                        //验证issn是否合法和分类号是否合法
                                        if( tag$code.equals("011$a") || tag$code.equals("690$a") ){
                                            Directory directory = new Directory();
                                            if(tag$code.equals("011$a")){
                                                directory.setIssn(data);
                                            }
                                            if(tag$code.equals("690$a")){
                                                directory.setLibrarsortCode(data);
                                            }
                                            Map<String,String> chechmap = checkPeriProperty(directory);
                                            for (String k : chechmap.keySet()) {//循环模板子字段
                                                if(chechmap.get(k) !=null){
                                                    return chechmap.get(k);
                                                }
                                            }
                                        }
                                        //首先要判断枚举里面是否存在
                                        if (suballfieldinfo != null && !suballfieldinfo.containsKey(code)) {
                                            return tag+"("+name+")马克子字段里不存在$"+code;
                                        }
                                        if(sfcfmap.containsKey(code)){
                                            return tag+"("+name+")"+code+"子字段重复";
                                        }else{
                                            sfcfmap.put(code,code);
                                        }
                                        //判断是否是必填项--只有模板里面才存在必填项不是模板不存在必填项之所
                                        if (tempsubfiledmap.containsKey(code)) {
                                            EnumMarcPeriSubfileInfo subfileInfo =  EnumMarcPeriSubfileInfo.getSubObject(tag,code);
                                            if (subfileInfo != null) {
                                                if(subfileInfo.getPeriNull()!=null && (subfileInfo.getPeriNull().equals("1"))){//必填
                                                    if (data.trim().equals("")) {
                                                        return tag+"("+name+")子字段$"+code+"("+subfileInfo.getName()+")为必填字段，不能为空！";
                                                    }
                                                }
                                                tempsubfiledmap.remove(code);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        for (Object o : tempsubfiledmap.keySet()) {//循环模板子字段
                            EnumMarcPeriSubfileInfo subfileInfo =  EnumMarcPeriSubfileInfo.getSubObject(tag,o.toString());
                            if (subfileInfo != null) {
                                return tag+"("+name+")子字段$"+o.toString()+"("+subfileInfo.getName()+")为模板必备字段不能缺少！";
                            }
                        }
                    }

                }else{
                    return tag+"马克字段不存在";
                }
            }
        }
        return null;
    }


    /**
     * 验证书目属性是否合法
     */
    public Map<String,String> checkPeriProperty(Directory directory) {
        Map<String, String> chechmap = new HashMap<>();
        //ISsN逻辑判断
        if (directory.getIssn() != null && !directory.getIssn().equals("")) {
            try{
                if (!SystemUtils.checkISSN(directory.getIssn())){
                    chechmap.put("fail", "Issn输入错误！");
                }
            }catch (Exception e){
                chechmap.put("fail",e.getMessage());
            }
        }
        return chechmap;
    }

    public Map<String,Object> checkMarcMap(List<Map> list){
        Map<String, Object> result = new HashMap<>();
        String str = this.checkMarc(list);
        if(str!=null){
            result.put("fail",str);
        }else{
            result.put("success","验证通过");
        }
        return result;
    }

    //这个是带复本数
    public Directory getDirectory(String id) {
        Directory directory = super.get(id);
        if(directory!=null){
            int i = findCountByBinding(id);
            if(i==0){
                //true是没有复本
                directory.setBookNum(true);
            }else {
                directory.setBookNum(false);
            }
        }
        return directory;
    }


    public Map<String,Object> fiveClassRate(String actionTime){
        String orgId = UserUtils.getOrgId();
        List<CirculateStatistics> crList = dao.fiveClassRate(actionTime, orgId);
        return SystemUtils.resolveCirculateRate(crList);
    }

    public Map<String,Object> bookDistributeRate(String actionTime,String orderBy){
        String orgId = UserUtils.getOrgId();
        List<CirculateStatistics> crList = dao.bookDistributeRate(actionTime, orgId, orderBy);
        return SystemUtils.resolveCirculateRate(crList);
    }

}