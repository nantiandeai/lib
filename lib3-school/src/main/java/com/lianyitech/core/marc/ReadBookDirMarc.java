package com.lianyitech.core.marc;

import com.lianyitech.core.enmu.EnumMarcInfo;
import com.lianyitech.core.enmu.EnumMarcPeriInfo;
import com.lianyitech.core.enmu.EnumMarcPeriSubfileInfo;
import com.lianyitech.core.enmu.EnumMarcSubfileInfo;
import com.lianyitech.common.utils.PropertiesLoader;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.utils.Compare;
import com.lianyitech.modules.peri.entity.Directory;
import org.marc4j.MarcPermissiveStreamReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by jordan jiang on 2016/8/31.
 * jordan jiang
 */
public class ReadBookDirMarc {
    private static Logger logger = LoggerFactory.getLogger(ReadBookDirMarc.class);
    public ReadBookDirMarc() {
    }
    String[] pz = new String[]{"平", "平装", "函装", "简装", "袋装", "压膜"};//这个是马克里面特别处理的
    String[] jz = new String[]{"精", "豪华精", "精装", "盒装"};//马克里面特别处理的
    private static Map<String, String> marcCodeMap = new HashMap<>();

    private List<BookDirectory> BookDirs = new ArrayList<>();

    private List<Copy> Copys = new ArrayList<>();

    public Map<String, String> getMarcCodeMap() {return marcCodeMap;}

    public void setMarcCodeMap(Map<String, String> marcCodeMap) {this.marcCodeMap = marcCodeMap;}

    public List<BookDirectory> getBookDirs() {return BookDirs;}

    public void setBookDirs(List<BookDirectory> bookDirs) {BookDirs = bookDirs;}

    public List<Copy> getCopys() {return Copys;}

    public void setCopys(List<Copy> copys) {Copys = copys;}
    static{
       readMarcProperties();
    }

    public static void readMarcProperties() {
        PropertiesLoader marcReadConf = new PropertiesLoader("marcConf.properties");

        // 返回Properties中包含的key-value的Set视图
        Set<Map.Entry<Object, Object>> set = marcReadConf.getProperties().entrySet();
        // 返回在此Set中的元素上进行迭代的迭代器
        Iterator<Map.Entry<Object, Object>> it = set.iterator();

        String key = null, value = null;
        while (it.hasNext()) {

            Map.Entry<Object, Object> entry = it.next();

            key = String.valueOf(entry.getKey());
            value = String.valueOf(entry.getValue());

            key = key == null ? key : key.trim();//.toUpperCase();
            value = value == null ? value : value.trim();//.toUpperCase();

            marcCodeMap.put(key, value);
        }
    }

    public void matchingField(String tagCode, String data, BookDirectory bookDir){

        if (marcCodeMap.get(tagCode)!=null){
            if (marcCodeMap.get(tagCode).equals("isbn")){
                bookDir.setIsbn(data);
            }else if (marcCodeMap.get(tagCode).equals("title")){
                bookDir.setTitle(data);
            }else if (marcCodeMap.get(tagCode).equals("subTitle")){
                bookDir.setSubTitle(data);
            }else if (marcCodeMap.get(tagCode).equals("tiedTitle")){
                bookDir.setTiedTitle(data);
            }else if (marcCodeMap.get(tagCode).equals("partNum")){
                bookDir.setPartNum(data);
            }else if (marcCodeMap.get(tagCode).equals("partName")){
                bookDir.setPartName(data);
            }else if (marcCodeMap.get(tagCode).equals("seriesName")){
                bookDir.setSeriesName(data);
            }else if (marcCodeMap.get(tagCode).equals("seriesEditor")){
                bookDir.setSeriesEditor(data);
            }else if (marcCodeMap.get(tagCode).equals("subAuthor")){
                bookDir.setSubAuthor(data);
            }else if (marcCodeMap.get(tagCode).equals("price")){
                String unitprice = data;
                if (data != null && !data.equals("") && !data.equals("")) {
                    unitprice = data.replaceAll("[`~!@#$^&*()=|{}':;',\\[\\]<>?~！@#￥……&*（）\\x20\u001e&mdash;—|{}【】‘；：”“'。，、？A-Za-z\u4e00-\u9fa5]", "");
                }
                bookDir.setUnitprice(unitprice);
            }else if (marcCodeMap.get(tagCode).equals("publishingAddress")){
                bookDir.setPublishingAddress(data);
            }else if (marcCodeMap.get(tagCode).equals("publishingName")){
                bookDir.setPublishingName(data);
            }else if (marcCodeMap.get(tagCode).equals("edition")){
                bookDir.setEdition(data);
            }else if (marcCodeMap.get(tagCode).equals("publishingTime")){
                bookDir.setPublishingTime(data);
            }else if (marcCodeMap.get(tagCode).equals("measure")){
                bookDir.setMeasure(data);
            }else if (marcCodeMap.get(tagCode).equals("bindingForm")){
                bookDir.setBindingForm(data);
            }else if (marcCodeMap.get(tagCode).equals("pageNo")){
                bookDir.setPageNo(data);
            }else if (marcCodeMap.get(tagCode).equals("content")){
                bookDir.setContent(data);
            }else if (marcCodeMap.get(tagCode).equals("librarsortCode")){
                bookDir.setLibrarsortCode(data);
            }else if (marcCodeMap.get(tagCode).equals("language")){
                bookDir.setLanguage(data);
            }else if (marcCodeMap.get(tagCode).equals("attachmentNote")){
                bookDir.setAttachmentNote(data);
            }else if (marcCodeMap.get(tagCode).equals("subject")){
                bookDir.setSubject(data);
            }else if (marcCodeMap.get(tagCode).equals("tanejiNo")){
                bookDir.setTanejiNo(data);
            }else if (marcCodeMap.get(tagCode).equals("author")){
                bookDir.setAuthor(data);
            }else if (marcCodeMap.get(tagCode).equals("Copy.barcode")){
                Copy copy = new Copy();
                copy.setBarcode(data);
                Copys.add(copy);
            }
        }
    }

    public List<BookDirectory> readMarcData(String path) throws FileNotFoundException, UnsupportedEncodingException {

        MarcPermissiveStreamReader reader = new MarcPermissiveStreamReader(new FileInputStream(path), true, false, "GBK");

        while (reader.hasNext()) {
            final Record record = reader.next();

            BookDirectory bookDir = new BookDirectory();

            List<DataField> dataFields = record.getDataFields();

            for(DataField dataField:dataFields){
                String tag = dataField.getTag();
                List<Subfield> subfields = dataField.getSubfields();
                for(Subfield subfield:subfields){
                    char code = subfield.getCode();
                    String data = subfield.getData();
                    try {
                        data = new String(subfield.getData().getBytes("ISO-8859-1"), "GBK");
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e.getMessage());
                    }

                    matchingField(tag + "$" + code, data, bookDir);
                    BookDirs.add(bookDir);

                    System.out.println(tag + "$" + code + "::::::" + data);
                }
            }
            System.out.println(new String(record.toString().getBytes("ISO-8859-1"), "GBK"));
        }

        return BookDirs;
    }

    public List<BookDirectory> readMarcDataLine(String path) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "GBK"));
        } catch (UnsupportedEncodingException e) {
            br.close();
           logger.error("操作失败",e);
        }

        String str;
        try {
            while((str=br.readLine())!=null) {

                InputStream inputS = new ByteArrayInputStream(str.getBytes("GBK"));
//                final MarcStreamReader reader = new MarcStreamReader(inputS, "GBK");
                MarcPermissiveStreamReader reader = new MarcPermissiveStreamReader(inputS, true, false, "GBK");
                while (reader.hasNext()) {

                    final Record record;
                    try{
                        record = reader.next();
                        //System.out.println(record.toString());

                        BookDirectory bookDir = new BookDirectory();

                        List<DataField> dataFields = record.getDataFields();

                        for(DataField dataField:dataFields){
                            String tag = dataField.getTag();
                            List<Subfield> subfields = dataField.getSubfields();
                            for(Subfield subfield:subfields){
                                char code = subfield.getCode();
                                String data = subfield.getData();

                                try {
                                    data = new String(subfield.getData().getBytes("ISO-8859-1"), "GBK");
                                } catch (UnsupportedEncodingException e) {
                                   logger.error("操作失败",e);
                                }

                                matchingField(tag + "$" + code, data, bookDir);
                               // System.out.println(tag + "$" + code + "::::::" + data);
                            }
                        }
                        BookDirs.add(bookDir);
                    }catch (Exception e){
                        logger.error("操作失败",e);
                    }
                }
            }
        } catch (IOException e) {
            br.close();
            logger.error("操作失败",e);
        }

        br.close();
        return BookDirs;
    }
    /*
     *   根据流读取一条马克数据信息同时解析成跟枚举关联的信息（将名称-必填-模板等信息解析出来）
     *   加了个实体类，如果存在值得情况下要以简单编目里面为准
   */
    public Object parseMarcLine(InputStream inputS,BookDirectory bookDirectory){
        List<Map> listmainfield = new ArrayList<>();//主表数据信息
        //得到模板字段信息map
        Map<String, Map<String,Object>> tempmarc =  EnumMarcInfo.getTempmarc();
        final MarcStreamReader reader = new MarcStreamReader(inputS, "GBK");
        //这里要将模板里面存在数据里面不存在的主字段数据信息加入进来
        parseMarcStreamtoList(reader,listmainfield,tempmarc,bookDirectory);
        for (Object o : tempmarc.keySet()) {
            listmainfield.add(getObjectMap(o.toString(),"", (Map) tempmarc.get(o.toString()),"",null,bookDirectory));
        }
        //下面是排序------------------------------------
        Collections.sort(listmainfield, Compare.mapCompare);
        //上面是排序------------------------------------------------
        return listmainfield;
    }




    public void parseMarcStreamtoList(MarcStreamReader reader,List<Map> listmainfield,Map<String, Map<String,Object>> tempmarc,BookDirectory bookDirectory){
        while (reader.hasNext()) {
            try {
                final Record record = reader.next();
                //得到头标区信息------头标区解析
                Leader leader = record.getLeader();//头标区
                listmainfield.add(getMainObjectMap("000","","头标区",leader.toString(),"0","1","1",null));
                if (tempmarc.containsKey("000")) {//头标区固定死的
                    tempmarc.remove("000");
                }
                //头标区解析
                //下面是解析control字段信息------------------------------------
                List<ControlField> controlFields = record.getControlFields();//control字段
                for (ControlField controlField : controlFields) {
                    String tag = controlField.getTag();
                    if (tempmarc.containsKey(tag)) {//存在主字段信息
                        listmainfield.add(getObjectMap(tag,"", (Map) tempmarc.get(tag),controlField.getData(),null,bookDirectory));
                        tempmarc.remove(tag);
                    } else {//不存在的情况下要到枚举里面去找
                        listmainfield.add(getObjectMap(tag,"", (Map) EnumMarcInfo.getMainNotSubFiled(tag),controlField.getData(),null,bookDirectory));
                    }
                }
                //下面是解析数据字段信息----------------------------------
                List<DataField> dataFields = record.getDataFields();//具体字段
                for(DataField dataField:dataFields){
                    String tag = dataField.getTag();
                    String ind1 = dataField.getIndicator1()+"";
                    List<Subfield> subfields = dataField.getSubfields();
                    if (tempmarc.containsKey(tag)) {//存在主字段信息
                        listmainfield.add(getObjectMap(tag,ind1,(Map) tempmarc.get(tag),"",subfields,bookDirectory));
                        tempmarc.remove(tag);
                    } else {//不存在的情况下要到枚举里面去找
                        listmainfield.add(getObjectMap(tag,ind1,(Map) EnumMarcInfo.getMainNotSubFiled(tag),"",subfields,bookDirectory));
                    }
                }
                //上面是解析数据字段信息-----------------------------------
            } catch (Exception e) {
               logger.error("操作失败",e);
            }
        }
    }




    public Map<String,Object> getMainObjectMap(String key,String ind1,String name,Object data ,String require,String ftype, String temp,List<String> list){
        Map<String, Object> map = new HashMap<>();
        //这里单独坐下验证005字段
        data = ConvertRecord.createMarc005(key,data).toString();
        map.put("tag",key);
        map.put("name",name);
        map.put("require",require);//必填
        map.put("temp",temp);
        map.put("data",data);
        map.put("ind1",ind1);
        map.put("ftype",ftype);
        if (list != null && list.size() > 0) {
            String datas = "";
            for(String str : list){
                datas+=str;
            }
            map.put("data",datas);
        }
        return map;
    }



    private String getSubString(String tag,String code,Object data,BookDirectory bookDirectory){
        String subdata = "";
        if(code!=null){
            subdata+="$"+code;
        }
        if (tag != null && !tag.equals("") && !subdata.equals("") && bookDirectory != null) {
          String  datastr =  getSubDataByBook(tag+subdata+"", bookDirectory);
            if(datastr!=null){
                data = datastr;
            }
        }
        subdata+=data;
        return subdata;
    }



    //根据简单编目解析具体的子字段数据--获得值（读取）
    public static String getSubDataByBook(String tagCode,BookDirectory bookDirectory){
        if (marcCodeMap.get(tagCode)!=null){
            Class cl = bookDirectory.getClass();//是Class，而不是class
            try {
                String methodname = getMname(tagCode);
                Method gMethod = cl.getMethod("get"+methodname);
                Object data = gMethod.invoke(bookDirectory);
                //这里要把字典值有关的解析下出来
                ReadBookDirMarc r = new ReadBookDirMarc();
                data = r.parseDicFiledtoMarc(methodname,data);
                if(data==null){
                    data = "";
                }
                return data.toString();
            } catch (Exception e) {
                logger.error("操作失败",e);
                return null;
            }
        }
        return null;
    }




    public  Object parseDicFiledtoBook(String methodname,Object data){//暂时写死
        if (data != null) {
            if (methodname.equals("Language")) {//语种
                if (data.equals("chi")){
                    data = "0";
                }else{
                    data = "1";
                }
            } else if (methodname.equals("BindingForm")) {
                List<String> pzlist = Arrays.asList(pz);
                List<String> jzlist = Arrays.asList(jz);
                if (pzlist.contains(data.toString())) {
                    data = "0";
                } else if (jzlist.contains(data.toString())) {
                    data = "1";
                } else if (data.equals("软精装") || data == "软精装") {
                    data = "2";
                } else {
                    data = "";
                }
            }
        }
        return data;
    }
    private  Object parseDicFiledtoMarc(String methodname,Object data){//暂时写死
        if (data != null) {
            if (methodname.equals("Language")) {//语种
                if (data.equals("0")){
                    data = "chi";
                }else{
                    data = "eng";
                }
            } else if (methodname.equals("BindingForm")) {
                if (data.equals("0")) {
                    data = "平装";
                } else if (data.equals("1")) {
                    data = "精装";
                } else {
                    data = "软精装";
                }
            }
        }
        return data;
    }

    //设置值
    public static void setSubDataByBook(String tagCode,BookDirectory bookDirectory,Object data){
        if (marcCodeMap.get(tagCode)!=null && bookDirectory!=null){
            Class cl = bookDirectory.getClass();//是Class，而不是class
            try {
                String methodname = getMname(tagCode);
                Method gMethod = cl.getMethod("get"+methodname);
                Method sMethod = cl.getMethod("set"+methodname,new Class[] {gMethod.getReturnType()});//这里修改了要用gMethod.getReturnType()，因为data从前端传过来的都是字符串

                //如果是价格要单独弄下
                if (("Unitprice").equals(methodname)) {
                    //正则那块很多地方要用到，时间允许的情况下到时候公用出来
                    data = data.toString().replaceAll("[`~!@#$^&*()=|{}':;',\\[\\]<>?~！@#￥……&*（）\\x20\u001e&mdash;—|{}【】‘；：”“'。，、？A-Za-z\u4e00-\u9fa5]", "");
                    data = Double.parseDouble(data.toString());
                    methodname = "Price";
                    gMethod = cl.getMethod("get" + methodname);
                    sMethod = cl.getMethod("set" + methodname, new Class[]{gMethod.getReturnType()});
                }
                    //这里要把字典值有关的解析下出来
                ReadBookDirMarc r = new ReadBookDirMarc();
                data = r.parseDicFiledtoBook(methodname,data);
                Object[] args1 = {data };
                // 参数列表
                sMethod.invoke(bookDirectory, args1);
            } catch (Exception e) {
                logger.error("操作失败",e);
               // System.out.print(e);d
            }
        }
    }



    private static String getMname(String tagCode) {
        String methodname = marcCodeMap.get(tagCode);
        if (methodname != null && !("".equals(methodname)) && methodname.length() > 0) {
            String first = methodname.substring(0, 1).toUpperCase();
            String rest = methodname.substring(1, methodname.length());
            methodname = first+rest;
        }
        return methodname;
    }
    //这里解析枚举中字段信息
    private Map<String,Object> getObjectMap(String key,String ind1,Map map,Object data,List<Subfield> list,BookDirectory bookDirectory){
        if ("000".equals(key)) {
            if (!(data != null && !("".equals(data.toString())))) {
                data = "nam0";//头标区写死
            }
        }
        List<String> listsubfield = new ArrayList<>();
        String name = "";
        String require = "";
        String ftype = "";
        String temp = "";
        if (map.containsKey("mainfiled")) {
            EnumMarcInfo mainfiled = (EnumMarcInfo) map.get("mainfiled");
            require = mainfiled.getNnull();
            name = mainfiled.getName();
            ftype = mainfiled.getFtype();
            temp = mainfiled.getTemp();
        }
        //模板子字段内容
        Map subfiledmap = new HashMap();
        if (map.get("subfiled")!=null) {
            subfiledmap.putAll((Map)map.get("subfiled"));
        }
        if (list != null && list.size() > 0) {
            for(Subfield subfield:list){//循环数据里面存在的数据信息
                String code = subfield.getCode()+"";
                String  subdata = subfield.getData();
                if (subfiledmap.containsKey(code)) {
                   EnumMarcSubfileInfo subfileInfo =  (EnumMarcSubfileInfo)subfiledmap.get(code);
                   if (subfileInfo != null) {
                        listsubfield.add(getSubString(key,code,subdata,bookDirectory));
                   }
                    subfiledmap.remove(code);
                }else{//去枚举里面查找对应的信息
                   EnumMarcSubfileInfo subfileInfo =  EnumMarcSubfileInfo.getSubObject(key,code);
                   if (subfileInfo != null) {//只有在枚举里面存在的情况下才加进来
                        listsubfield.add(getSubString(key,code,subdata,bookDirectory));
                   }
                }
            }
        }
        //这里要将模板里面存在数据里面不存在的数据信息加入进来
        for (Object o : subfiledmap.keySet()) {
            EnumMarcSubfileInfo subfileInfo =  EnumMarcSubfileInfo.getSubObject(key,o.toString());
            if (subfileInfo != null) {
                listsubfield.add(getSubString(key,o.toString(),"",bookDirectory));
            }
        }
        //下面是排序-----------------------------
        Collections.sort(listsubfield);
        //上面是排序--------------------------
        //上面是子字段内容
       return  this.getMainObjectMap(key,ind1,name,data,require,ftype,temp,listsubfield);
    }


    //根据流读取马克数据信息将结果集放入到list集合
    public static List<Record> readerMarcStreamToList(InputStream inputS) {
        List<Record> list = new ArrayList<>();
        final MarcStreamReader reader = new MarcStreamReader(inputS, "GBK");
        while (reader.hasNext()) {
               list.add(reader.next());
        }
        return list;
    }

    public static void main(final String args[]) throws Exception {
        ReadBookDirMarc readBookDirMarc = new ReadBookDirMarc();
        readBookDirMarc.readMarcDataLine("F:/图书管理系统/图书3.0/马克数据/test.iso");
    }
}
