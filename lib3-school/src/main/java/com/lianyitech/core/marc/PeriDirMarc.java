package com.lianyitech.core.marc;

import com.lianyitech.common.utils.PropertiesLoader;
import com.lianyitech.core.enmu.EnumMarcInfo;
import com.lianyitech.core.enmu.EnumMarcPeriInfo;
import com.lianyitech.core.enmu.EnumMarcPeriSubfileInfo;
import com.lianyitech.core.enmu.EnumMarcSubfileInfo;
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
public class PeriDirMarc {
    private static Logger logger = LoggerFactory.getLogger(PeriDirMarc.class);
    public PeriDirMarc() {
    }
    String[] pz = new String[]{"平", "平装", "函装", "简装", "袋装", "压膜"};//这个是马克里面特别处理的
    String[] jz = new String[]{"精", "豪华精", "精装", "盒装"};//马克里面特别处理的
    private List<Directory> periDirs = new ArrayList<>();

    public List<Directory> getPeriDirs() {
        return periDirs;
    }

    public void setPeriDirs(List<Directory> periDirs) {
        this.periDirs = periDirs;
    }

    private static Map<String, String> marcCodeMap = new HashMap<>();


    public Map<String, String> getMarcCodeMap() {return marcCodeMap;}

    public void setMarcCodeMap(Map<String, String> marcCodeMap) {this.marcCodeMap = marcCodeMap;}

    static{
       readMarcProperties();
    }

    public static void readMarcProperties() {
        PropertiesLoader marcReadConf = new PropertiesLoader("marcReadConf.properties");

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


    /*
    *   期刊
  */
    public Object parseMarcLine(InputStream inputS,Directory directory){
        List<Map> listmainfield = new ArrayList<>();//主表数据信息
        //得到模板字段信息map
        Map<String, Map<String,Object>> tempmarc =  EnumMarcPeriInfo.getTempmarc();
        final MarcStreamReader reader = new MarcStreamReader(inputS, "GBK");
        //这里要将模板里面存在数据里面不存在的主字段数据信息加入进来
        parseMarcStreamtoList(reader,listmainfield,tempmarc,directory);
        for (Object o : tempmarc.keySet()) {
            listmainfield.add(getObjectMap(o.toString(),"", (Map) tempmarc.get(o.toString()),"",null,directory));
        }
        //下面是排序------------------------------------
        Collections.sort(listmainfield, Compare.mapCompare);
        //上面是排序------------------------------------------------
        return listmainfield;
    }

    /**
     * 期刊
     * @param reader
     * @param listmainfield
     * @param tempmarc
     * @param directory
     */
    public void parseMarcStreamtoList(MarcStreamReader reader,List<Map> listmainfield,Map<String, Map<String,Object>> tempmarc,Directory directory){
        while (reader.hasNext()) {
            try {
                final Record record = reader.next();
                //得到头标区信息------头标区解析
                Leader leader = record.getLeader();//头标区
                listmainfield.add(getPeriMainObjectMap("000","","头标区",leader.toString(),"0","1","1",null,"1","1"));
                if (tempmarc.containsKey("000")) {//头标区固定死的
                    tempmarc.remove("000");
                }
                //头标区解析
                //下面是解析control字段信息------------------------------------
                List<ControlField> controlFields = record.getControlFields();//control字段
                for (ControlField controlField : controlFields) {
                    String tag = controlField.getTag();
                    if (tempmarc.containsKey(tag)) {//存在主字段信息
                        listmainfield.add(getObjectMap(tag,"", (Map) tempmarc.get(tag),controlField.getData(),null,directory));
                        tempmarc.remove(tag);
                    } else {//不存在的情况下要到枚举里面去找
                        listmainfield.add(getObjectMap(tag,"", (Map) EnumMarcPeriInfo.getMainNotSubFiled(tag),controlField.getData(),null,directory));
                    }
                }
                //下面是解析数据字段信息----------------------------------
                List<DataField> dataFields = record.getDataFields();//具体字段
                for(DataField dataField:dataFields){
                    String tag = dataField.getTag();
                    String ind1 = dataField.getIndicator1()+"";
                    List<Subfield> subfields = dataField.getSubfields();
                    if (tempmarc.containsKey(tag)) {//存在主字段信息
                        listmainfield.add(getObjectMap(tag,ind1,(Map) tempmarc.get(tag),"",subfields,directory));
                        tempmarc.remove(tag);
                    } else {//不存在的情况下要到枚举里面去找
                        listmainfield.add(getObjectMap(tag,ind1,(Map) EnumMarcPeriInfo.getMainNotSubFiled(tag),"",subfields,directory));
                    }
                }
                //上面是解析数据字段信息-----------------------------------
            } catch (Exception e) {
               logger.error(e.getMessage());
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

    public Map<String,Object> getPeriMainObjectMap(String key,String ind1,String name,Object data ,String require,String ftype, String temp,List<String> list,
                                                    String periTemp,String perNull){
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
        map.put("periTemp",periTemp);
        map.put("perNull",perNull);
        if (list != null && list.size() > 0) {
            String datas = "";
            for(String str : list){
                datas+=str;
            }
            map.put("data",datas);
        }
        return map;
    }


    /**
     * 测试
     * @param tag
     * @param code
     * @param data
     * @param directory
     * @return
     */
    private String getSubString(String tag,String code,Object data,Directory directory){
        String subdata = "";
        if(code!=null){
            subdata+="$"+code;
        }
        if (tag != null && !tag.equals("") && !subdata.equals("") && directory != null) {
            String  datastr =  getSubDataByBook(tag+subdata+"", directory);
            if(datastr!=null){
                data = datastr;
            }
        }
        subdata+=data;
        return subdata;
    }


    /**
     * 测试
     * @param tagCode
     * @param directory
     * @return
     */
    //根据简单编目解析具体的子字段数据--获得值（读取）
    public static String getSubDataByBook(String tagCode,Directory directory){
        if (marcCodeMap.get(tagCode)!=null){
            Class cl = directory.getClass();//是Class，而不是class
            try {
                String methodname = getMname(tagCode);
                Method gMethod = cl.getMethod("get"+methodname);
                Object data = gMethod.invoke(directory);
                //这里要把字典值有关的解析下出来
                PeriDirMarc r = new PeriDirMarc();
                data = r.parseDicFiledtoMarc(methodname,data);
                if(data==null){
                    data = "";
                }
                return data.toString();
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }
        return null;
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


    /**
     * 这里是测试沿用以前的有没有用
     * @param tagCode
     * @param directory
     * @param data
     */
    //设置值
    public static void setSubDataByBook(String tagCode, Directory directory, Object data){
        if (marcCodeMap.get(tagCode)!=null && directory!=null){
            Class cl = directory.getClass();//是Class，而不是class
            try {
                String methodname = getMname(tagCode);
                Method gMethod = cl.getMethod("get"+methodname);
                Method sMethod = cl.getMethod("set"+methodname,new Class[] {gMethod.getReturnType()});//这里修改了要用gMethod.getReturnType()，因为data从前端传过来的都是字符串
                //如果是价格要单独弄下
                if (("Price").equals(methodname)) {
                    //正则那块很多地方要用到，时间允许的情况下到时候公用出来
                    data  = data.toString().replaceAll("[`~!@#$^&*()=|{}':;',\\[\\]<>?~！@#￥……&*（）\\x20\u001e&mdash;—|{}【】‘；：”“'。，、？A-Za-z\u4e00-\u9fa5]", "");
                    data = Double.parseDouble(data.toString());
                }
                //这里要把字典值有关的解析下出来
                PeriDirMarc r = new PeriDirMarc();
                data = r.parseDicFiledtoBook(methodname,data);
                Object[] args1 = {data };
                // 参数列表
                sMethod.invoke(directory, args1);
            } catch (Exception e) {
                logger.error(e.getMessage());
                // System.out.print(e);
            }
        }
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

    private static String getMname(String tagCode) {
        String methodname = marcCodeMap.get(tagCode);
        if (methodname != null && !("".equals(methodname)) && methodname.length() > 0) {
            String first = methodname.substring(0, 1).toUpperCase();
            String rest = methodname.substring(1, methodname.length());
            methodname = first+rest;
        }
        return methodname;
    }
    /**
     * 测试
     * @param key
     * @param ind1
     * @param map
     * @param data
     * @param list
     * @param directory
     * @return
     */
    private Map<String,Object> getObjectMap(String key,String ind1,Map map,Object data,List<Subfield> list,Directory directory){
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
        String periTemp = "";
        String perNull = "";
        if (map.containsKey("mainfiled")) {
            EnumMarcPeriInfo mainfiled = (EnumMarcPeriInfo) map.get("mainfiled");
            require = mainfiled.getNnull();
            name = mainfiled.getName();
            ftype = mainfiled.getFtype();
            temp = mainfiled.getTemp();
            periTemp = mainfiled.getPeriTemp();
            perNull = mainfiled.getPerNull();
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
                    EnumMarcPeriSubfileInfo subfileInfo =  (EnumMarcPeriSubfileInfo)subfiledmap.get(code);
                    if (subfileInfo != null) {
                        listsubfield.add(getSubString(key,code,subdata,directory));
                    }
                    subfiledmap.remove(code);
                }else{//去枚举里面查找对应的信息
                    EnumMarcPeriSubfileInfo subfileInfo =  EnumMarcPeriSubfileInfo.getSubObject(key,code);
                    if (subfileInfo != null) {//只有在枚举里面存在的情况下才加进来
                        listsubfield.add(getSubString(key,code,subdata,directory));
                    }
                }
            }
        }
        //这里要将模板里面存在数据里面不存在的数据信息加入进来
        for (Object o : subfiledmap.keySet()) {
            EnumMarcSubfileInfo subfileInfo =  EnumMarcSubfileInfo.getSubObject(key,o.toString());
            if (subfileInfo != null) {
                listsubfield.add(getSubString(key,o.toString(),"",directory));
            }
        }
        //下面是排序-----------------------------
        Collections.sort(listsubfield);
        //上面是排序--------------------------
        //上面是子字段内容
        return  this.getPeriMainObjectMap(key,ind1,name,data,require,ftype,temp,listsubfield,periTemp,perNull);
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

}
