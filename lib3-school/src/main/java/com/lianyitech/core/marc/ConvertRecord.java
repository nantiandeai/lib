package com.lianyitech.core.marc;

import com.lianyitech.core.enmu.EnumMarcInfo;
import com.lianyitech.core.enmu.EnumMarcPeriSubfileInfo;
import com.lianyitech.core.enmu.EnumMarcSubfileInfo;
import com.lianyitech.core.enmu.EnumMarcPeriInfo;
import com.lianyitech.modules.catalog.utils.Compare;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.peri.entity.Directory;
import info.freelibrary.marc4j.impl.SubfieldImpl;
import org.apache.commons.io.IOUtils;
import org.marc4j.marc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zengyuanmei on 2016/9/8.
 * 将特定的数据格式转换对应马克解析里实体record对象
 */
public class ConvertRecord {
    private static Logger logger = LoggerFactory.getLogger(ConvertRecord.class);
    private static MarcFactory factory;
    static {
        factory = MarcFactory.newInstance();
     }
     //根据简单编目实体类将其对应的马克数据信息中跟简单编目对应的属性修改
    public static Record makeRecordfromBookDirectory(BookDirectory bookDirectory){
        //第一部分要判断是否该条书目是否存在马克数据（如果修改一遍情况下存在--添加是不可能存在的，这里通用判断）
        String marcdata = "";
        if (bookDirectory != null && bookDirectory.getMarc64() != null && !"".equals(bookDirectory.getMarc64())) {
            marcdata = bookDirectory.getMarc64();
        }
        //得到模板字段信息map
        Map<String, Map<String,Object>> tempmarc =  EnumMarcInfo.getTempmarc();
        Record record = factory.newRecord("00971nam0 2200265    450");//暂时写死头标去
        if (!"".equals(marcdata)) {
            InputStream inputS = null;
            try {
                inputS = new ByteArrayInputStream(marcdata.getBytes("GBK"));
                List<Record> records = ReadBookDirMarc.readerMarcStreamToList(inputS);
                if (records != null && records.size() > 0) {
                    record = records.get(0);
                   setRecordbycon(record,bookDirectory,tempmarc);//将简单编目信息重新赋值到马克record记录
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("操作失败",e);
            } catch (Exception e) {
                logger.error("操作失败",e);
            } finally {
                IOUtils.closeQuietly(inputS);
            }
        }
        //循环模板里面的数据--如果存在已有的马克字段信息这里tempmarc已经去掉了
        for (Object o : tempmarc.keySet()) {
            if (o != null && !("".equals(o.toString()))) {
                if(!("000".equals(o.toString()))){//去掉头标区
                    String type = "3";//默认为数据字段
                    Map map = (Map) tempmarc.get(o.toString());
                    if (map.containsKey("mainfiled")) {
                        EnumMarcInfo mainfiled = (EnumMarcInfo) map.get("mainfiled");
                        type = mainfiled.getFtype();
                    }
                   /* if (type == "1") {
                    } else */
                    if ("2".equals(type)) {//control字段
                        String d = createMarc005(o.toString(),"").toString();
                        ControlField controlField = factory.newControlField(o.toString(), d);
                        record.addVariableField(controlField);
                    } else {//数据字段
                        DataField d = setDataFieldInfo(o.toString(),null,bookDirectory,tempmarc.get(o.toString()));
                        record.addVariableField(d);//将主字段信息加进来
                    }
                }
            }
        }
        //下面数据主字段是排序------------------------------------
        Collections.sort(record.getDataFields(), Compare.dataComparator);
        //上面是数据主字段信息排序
        return record;
    }

    /**
     * 测试
     * @param directory
     * @return
     */
    //根据简单编目实体类将其对应的马克数据信息中跟简单编目对应的属性修改
    public static Record makeRecordfromBookDirectory(Directory directory){
        //第一部分要判断是否该条书目是否存在马克数据（如果修改一遍情况下存在--添加是不可能存在的，这里通用判断）
        String marcdata = "";
        if (directory != null && directory.getMarc64() != null && !"".equals(directory.getMarc64())) {
            marcdata = directory.getMarc64();
        }
        //得到模板字段信息map
        Map<String, Map<String,Object>> tempmarc =  EnumMarcPeriInfo.getTempmarc();
        Record record = factory.newRecord("00971nam0 2200265    450");//暂时写死头标去
        if (!"".equals(marcdata)) {
            InputStream inputS = null;
            try {
                inputS = new ByteArrayInputStream(marcdata.getBytes("GBK"));
                List<Record> records = ReadBookDirMarc.readerMarcStreamToList(inputS);
                if (records != null && records.size() > 0) {
                    record = records.get(0);
                    setRecordbycon(record,directory,tempmarc);//将简单编目信息重新赋值到马克record记录
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("操作失败",e);
            } catch (Exception e) {
                logger.error("操作失败",e);
            } finally {
                IOUtils.closeQuietly(inputS);
            }
        }
        //循环模板里面的数据--如果存在已有的马克字段信息这里tempmarc已经去掉了
        for (Object o : tempmarc.keySet()) {
            if (o != null && !("".equals(o.toString()))) {
                if(!("000".equals(o.toString()))){//去掉头标区
                    String type = "3";//默认为数据字段
                    Map map = (Map) tempmarc.get(o.toString());
                    if (map.containsKey("mainfiled")) {
                        EnumMarcPeriInfo mainfiled = (EnumMarcPeriInfo) map.get("mainfiled");
                        type = mainfiled.getFtype();
                    }
                   /* if (type == "1") {
                    } else */
                    if ("2".equals(type)) {//control字段
                        String d = createMarc005(o.toString(),"").toString();
                        ControlField controlField = factory.newControlField(o.toString(), d);
                        record.addVariableField(controlField);
                    } else {//数据字段
                        DataField d = setDataFieldInfo(o.toString(),null,directory,tempmarc.get(o.toString()));
                        record.addVariableField(d);//将主字段信息加进来
                    }
                }
            }
        }
        //下面数据主字段是排序------------------------------------
        Collections.sort(record.getDataFields(), Compare.dataComparator);
        //上面是数据主字段信息排序
        return record;
    }
    //这里将已有的马克数据record记录重新赋值
    private static void setRecordbycon(Record existrecord,BookDirectory bookDirectory,Map<String, Map<String,Object>> tempmarc){
        if (existrecord != null) {
            //这里忽略掉头标区--头标为000 上面写死了目前，去掉模板里面对应的000
            if (tempmarc.containsKey("000")) {//头标区固定死的
                tempmarc.remove("000");
            }
            //controlFiled区解析-------------------------------------------
            List<ControlField> controlFields = existrecord.getControlFields();//control字段
            for (ControlField controlField : controlFields) {
                String tag = controlField.getTag();
                //005字段
                if(tag.equals("005")){
                     controlField.setData(createMarc005(tag,controlField.getData()).toString());
                }
                if (tempmarc.containsKey(tag)) {//存在主字段信息
                    tempmarc.remove(tag);//模板里面要替换省的下面重新加重复的记录
                }
            }
            //controlfiled区解析结束--------------------------------------
            //下面是数据字段区解析-------这块最关键，这里就需要替换内容数据了
            List<DataField> dataFields = existrecord.getDataFields();//具体字段
            for(DataField dataField:dataFields){
                String tag = dataField.getTag();
              //  List<Subfield> subfields = dataField.getSubfields();
                if (tempmarc.containsKey(tag)) {//模板里面存在主字段信息
                    setDataFieldInfo(tag,dataField,bookDirectory,tempmarc.get(tag));
                    tempmarc.remove(tag);
                } else {//模板里面不存在主字段的情况到枚举里面去找
                    setDataFieldInfo(tag,dataField,bookDirectory, EnumMarcInfo.getMainNotSubFiled(tag));
                }
            }
            //上面是数据字段区解析
        }
    }

    /**
     * 测试
     * @param existrecord
     * @param directory
     * @param tempmarc
     */
    //这里将已有的马克数据record记录重新赋值
    private static void setRecordbycon(Record existrecord,Directory directory,Map<String, Map<String,Object>> tempmarc){
        if (existrecord != null) {
            //这里忽略掉头标区--头标为000 上面写死了目前，去掉模板里面对应的000
            if (tempmarc.containsKey("000")) {//头标区固定死的
                tempmarc.remove("000");
            }
            //controlFiled区解析-------------------------------------------
            List<ControlField> controlFields = existrecord.getControlFields();//control字段
            for (ControlField controlField : controlFields) {
                String tag = controlField.getTag();
                //005字段
                if(tag.equals("005")){
                    controlField.setData(createMarc005(tag,controlField.getData()).toString());
                }
                if (tempmarc.containsKey(tag)) {//存在主字段信息
                    tempmarc.remove(tag);//模板里面要替换省的下面重新加重复的记录
                }
            }
            //controlfiled区解析结束--------------------------------------
            //下面是数据字段区解析-------这块最关键，这里就需要替换内容数据了
            List<DataField> dataFields = existrecord.getDataFields();//具体字段
            for(DataField dataField:dataFields){
                String tag = dataField.getTag();
                //  List<Subfield> subfields = dataField.getSubfields();
                if (tempmarc.containsKey(tag)) {//模板里面存在主字段信息
                    setDataFieldInfo(tag,dataField,directory,tempmarc.get(tag));
                    tempmarc.remove(tag);
                } else {//模板里面不存在主字段的情况到枚举里面去找
                    setDataFieldInfo(tag,dataField,directory, EnumMarcPeriInfo.getMainNotSubFiled(tag));
                }
            }
            //上面是数据字段区解析
        }
    }

    //主字段重新设置，替换里面字段内容，如果子字段不存在模板子字段要加进来
    private static DataField setDataFieldInfo(String tag,DataField dataField,BookDirectory bookDirectory,Map<String,Object> map){
        if(dataField==null){
            dataField = factory.newDataField(tag, ' ', ' ');
        }
        List<Subfield> subfields = dataField.getSubfields();
        //模板子字段内容
        Map subfiledmap = new HashMap<>();
        if (map.get("subfiled")!=null) {
                subfiledmap.putAll((Map) map.get("subfiled"));
        }
        if (subfields != null && subfields.size() > 0) {
            for(Subfield subfield:subfields){//循环数据里面存在的数据信息
                if (subfield != null) {
                    String code = subfield.getCode()+"";
                    if (subfiledmap.containsKey(code)) {
                        EnumMarcSubfileInfo subfileInfo =  (EnumMarcSubfileInfo)subfiledmap.get(code);
                        if (subfileInfo != null) {
                            setSubfieldData(tag, subfield, bookDirectory);
                            subfiledmap.remove(code);
                        }else{
                            subfields.remove(subfield);//如果是垃圾数据则删除掉
                        }

                    }else{//去枚举里面查找对应的信息
                        EnumMarcSubfileInfo subfileInfo =  EnumMarcSubfileInfo.getSubObject(tag,code);
                        if (subfileInfo != null) {//只有在枚举里面存在的情况下才加进来
                            setSubfieldData(tag, subfield, bookDirectory);
                        } else {//如果枚举里面不存在这子字段则是垃圾数据删除掉
                            subfields.remove(subfield);
                        }
                    }
                }
            }
        }
        //这里要将模板里面存在数据里面不存在的数据信息加入进来
        for (Object o : subfiledmap.keySet()) {
            EnumMarcSubfileInfo subfileInfo =  EnumMarcSubfileInfo.getSubObject(tag,o.toString());
            if (subfileInfo != null) {
                char c = ' ';
                if (o.toString().length() > 0) {
                    c = o.toString().charAt(0);
                }
                Subfield sub = new SubfieldImpl(c, "");
                setSubfieldData(tag, sub, bookDirectory);
                if(subfields!=null){
                    subfields.add(sub);
                }
            }
        }
        //下面sub字段是排序-----------------------------
        if (subfields != null) {
            Collections.sort(subfields, Compare.subfieldComparator);
        }
        //上面是sub字段排序--------------------------
        return dataField;
    }

    /**
     * 测试
     * @param tag
     * @param dataField
     * @param directory
     * @param map
     * @return
     */
    //主字段重新设置，替换里面字段内容，如果子字段不存在模板子字段要加进来
    private static DataField setDataFieldInfo(String tag,DataField dataField,Directory directory,Map<String,Object> map){
        if(dataField==null){
            dataField = factory.newDataField(tag, ' ', ' ');
        }
        List<Subfield> subfields = dataField.getSubfields();
        //模板子字段内容
        Map subfiledmap = new HashMap<>();
        if (map.get("subfiled")!=null) {
            subfiledmap.putAll((Map) map.get("subfiled"));
        }
        if (subfields != null && subfields.size() > 0) {
            for(Subfield subfield:subfields){//循环数据里面存在的数据信息
                if (subfield != null) {
                    String code = subfield.getCode()+"";
                    if (subfiledmap.containsKey(code)) {
                        EnumMarcPeriSubfileInfo subfileInfo =  (EnumMarcPeriSubfileInfo)subfiledmap.get(code);
                        if (subfileInfo != null) {
                            setSubfieldData(tag, subfield, directory);
                            subfiledmap.remove(code);
                        }else{
                            subfields.remove(subfield);//如果是垃圾数据则删除掉
                        }

                    }else{//去枚举里面查找对应的信息
                        EnumMarcPeriSubfileInfo subfileInfo =  EnumMarcPeriSubfileInfo.getSubObject(tag,code);
                        if (subfileInfo != null) {//只有在枚举里面存在的情况下才加进来
                            setSubfieldData(tag, subfield, directory);
                        } else {//如果枚举里面不存在这子字段则是垃圾数据删除掉
                            subfields.remove(subfield);
                        }
                    }
                }
            }
        }
        //这里要将模板里面存在数据里面不存在的数据信息加入进来
        for (Object o : subfiledmap.keySet()) {
            EnumMarcPeriSubfileInfo subfileInfo =  EnumMarcPeriSubfileInfo.getSubObject(tag,o.toString());
            if (subfileInfo != null) {
                char c = ' ';
                if (o.toString().length() > 0) {
                    c = o.toString().charAt(0);
                }
                Subfield sub = new SubfieldImpl(c, "");
                setSubfieldData(tag, sub, directory);
                if(subfields!=null){
                    subfields.add(sub);
                }
            }
        }
        //下面sub字段是排序-----------------------------
        if (subfields != null) {
            Collections.sort(subfields, Compare.subfieldComparator);
        }
        //上面是sub字段排序--------------------------
        return dataField;
    }

    //重新设置subfield的值
    private static void setSubfieldData(String tag,Subfield subfield,BookDirectory bookDirectory){
        String subdata = "";
        String code = subfield.getCode()+"";
        subdata+="$"+code;
        if (tag != null && !"".equals(tag) && !"".equals(subdata) && bookDirectory != null) {
            String  datastr =  ReadBookDirMarc.getSubDataByBook(tag+subdata+"", bookDirectory);
            if(datastr!=null){
                subfield.setData(datastr);
            }
        }
    }

    /**
     * 测试
     * @param tag
     * @param subfield
     * @param directory
     */
    //重新设置subfield的值
    private static void setSubfieldData(String tag,Subfield subfield,Directory directory){
        String subdata = "";
        String code = subfield.getCode()+"";
        subdata+="$"+code;
        if (tag != null && !"".equals(tag) && !"".equals(subdata) && directory != null) {
            String  datastr =  PeriDirMarc.getSubDataByBook(tag+subdata+"", directory);
            if(datastr!=null){
                subfield.setData(datastr);
            }
        }
    }


    //通过前端传过来的request里面的属性进行转成具体的record---（这个方法是针对前端传过来参数保存马克数据信息）
    public static Record makeRecordfromRequest(BookDirectory bookDirectory, HttpServletRequest request){
        String[] mainfieds = request.getParameterValues("mainfield");
        if (mainfieds != null && mainfieds.length > 0) {
            final Record sumland = factory.newRecord("00971nam0 2200265    450");//00971nam  2200265   450
            for(String mainfiled : mainfieds){
                String ind = request.getParameter("ind"+mainfiled);
                char indx1 = ' ';
                if(ind!=null && ind.length()>0){
                    indx1 = ind.charAt(0);
                }
                String sub = request.getParameter("sub"+mainfiled);
                Map<String, Object> mapinfo =  EnumMarcInfo.getMainSubFiled(mainfiled);
                if (mapinfo != null) {
                    EnumMarcInfo marcInfo =  (EnumMarcInfo)mapinfo.get("mainfiled");
                    //Map suballfieldinfo = (Map)mapinfo.get("subfiled");
                    if (marcInfo != null) {//主字段存在的情况
                        String type = marcInfo.getFtype();
                        /*if(("1").equals(type)){//leader //这里目前不需要做啥处理(上面leader头写死了)
                        } else*/
                        if(("2").equals(type)){//controlfield
                            sumland.addVariableField(factory.newControlField(mainfiled, sub));
                        }else if("3".equals(type)){//datafield
                            DataField d = factory.newDataField(mainfiled,indx1, ' ');
                            List<Subfield> subfields =  d.getSubfields();
                            //这里要解析对应的子字段信息
                            setSubFiled(subfields,sub,mainfiled,bookDirectory);
                            sumland.addVariableField(d);
                        }
                    }
                }
            }
            return sumland;
        }

        return null;
    }

    //通过前端传过来的request里面的属性进行转成具体的record---（这个方法是针对前端传过来参数保存马克数据信息）

    /**
     * 这里是测试没用以前的有没有用
     * @param directory
     * @param request
     * @return
     */
    public static Record makeRecordfromRequest(Directory directory, HttpServletRequest request){
        String[] mainfieds = request.getParameterValues("mainfield");
        if (mainfieds != null && mainfieds.length > 0) {
            final Record sumland = factory.newRecord("00971nam0 2200265    450");//00971nam  2200265   450
            for(String mainfiled : mainfieds){
                String ind = request.getParameter("ind"+mainfiled);
                char indx1 = ' ';
                if(ind!=null && ind.length()>0){
                    indx1 = ind.charAt(0);
                }
                String sub = request.getParameter("sub"+mainfiled);
                Map<String, Object> mapinfo =  EnumMarcPeriInfo.getMainSubFiled(mainfiled);
                if (mapinfo != null) {
                    EnumMarcPeriInfo marcInfo =  (EnumMarcPeriInfo)mapinfo.get("mainfiled");
                    //Map suballfieldinfo = (Map)mapinfo.get("subfiled");
                    if (marcInfo != null) {//主字段存在的情况
                        String type = marcInfo.getFtype();
                        /*if(("1").equals(type)){//leader //这里目前不需要做啥处理(上面leader头写死了)
                        } else*/
                        if(("2").equals(type)){//controlfield
                            sumland.addVariableField(factory.newControlField(mainfiled, sub));
                        }else if("3".equals(type)){//datafield
                            DataField d = factory.newDataField(mainfiled,indx1, ' ');
                            List<Subfield> subfields =  d.getSubfields();
                            //这里要解析对应的子字段信息
                            setSubFiled(subfields,sub,mainfiled,directory);
                            sumland.addVariableField(d);
                        }
                    }
                }
            }
            return sumland;
        }

        return null;
    }

    /*
    根据字符串解析成子字段添加到list集合这里将枚举里面的字段的信息放进来做判断区分是否添加的信息有问题,
    （加了个bookDirectory 要将马克数据信息对应内容放入到bookDirectory里面）
   */
    private static List<Subfield> setSubFiled(List<Subfield> subfields, String subfiledstr, String mainfiled, BookDirectory bookDirectory) {
        //获取模板信息--------开始
        Map suballfieldinfo = null;
        Map<String, Object> mapinfo = EnumMarcInfo.getMainSubFiled(mainfiled);
        if (mapinfo != null) {
          //  EnumMarcInfo marcInfo = (EnumMarcInfo) mapinfo.get("mainfiled");
            suballfieldinfo = (Map) mapinfo.get("subfiled");
        }
        //获取模板信息-------结束
        //这里首先要截取首字母不是$的
        int index_s = subfiledstr.indexOf("$");
        if (index_s >= 0) {
            subfiledstr = subfiledstr.substring(index_s);
        }
        String[] substrs = subfiledstr.split("\\$");
        if (substrs.length > 0) {
            for (String substr : substrs) {
                    if ( substr.length() > 1) {
                        String code = substr.substring(0, 1);
                        String data = substr.substring(1);
                        if (suballfieldinfo != null && suballfieldinfo.containsKey(code)) {
                            subfields.add(new SubfieldImpl(code.charAt(0), data));
                            //这里需要重新去设置bookDirectory的值
                            ReadBookDirMarc.setSubDataByBook(mainfiled+"$"+code,bookDirectory,data);
                        }
                }

            }
        }
        //排序
        Collections.sort(subfields, Compare.subfieldComparator);
        return subfields;
    }

    /*
   根据字符串解析成子字段添加到list集合这里将枚举里面的字段的信息放进来做判断区分是否添加的信息有问题,
   （加了个bookDirectory 要将马克数据信息对应内容放入到bookDirectory里面）
  */

    /**
     * 这里是测试沿用以前的有没有用
     * @param subfields
     * @param subfiledstr
     * @param mainfiled
     * @param directory
     * @return
     */
    private static List<Subfield> setSubFiled(List<Subfield> subfields, String subfiledstr, String mainfiled, Directory directory) {
        //获取模板信息--------开始
        Map suballfieldinfo = null;
        Map<String, Object> mapinfo = EnumMarcPeriInfo.getMainSubFiled(mainfiled);
        if (mapinfo != null) {
            //  EnumMarcInfo marcInfo = (EnumMarcInfo) mapinfo.get("mainfiled");
            suballfieldinfo = (Map) mapinfo.get("subfiled");
        }
        //获取模板信息-------结束
        //这里首先要截取首字母不是$的
        int index_s = subfiledstr.indexOf("$");
        if (index_s >= 0) {
            subfiledstr = subfiledstr.substring(index_s);
        }
        String[] substrs = subfiledstr.split("\\$");
        if (substrs.length > 0) {
            for (String substr : substrs) {
                if ( substr.length() > 1) {
                    String code = substr.substring(0, 1);
                    String data = substr.substring(1);
                    if (suballfieldinfo != null && suballfieldinfo.containsKey(code)) {
                        subfields.add(new SubfieldImpl(code.charAt(0), data));
                        //这里需要重新去设置bookDirectory的值
                        PeriDirMarc.setSubDataByBook(mainfiled+"$"+code,directory,data);
                    }
                }
            }
        }
        //排序
        Collections.sort(subfields, Compare.subfieldComparator);
        return subfields;
    }

    //转换公共部分通过record记录转成String
    public static String commonConvertMarcData(Record record){
        String marcdata = "";
        Record records[] = null;
        if (record != null) {
            records = new Record[1];
            records[0] = record;
        }
        ByteArrayOutputStream out = WriterBookMarc.writerMarcToByteStream(records);
        if (out != null) {
            try {
                marcdata = new String(out.toByteArray(), "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return marcdata;
    }
    public static Object createMarc005(String tag,Object data){//动态生成马克005字段
        if (tag != null && tag.equals("005")) {
            if (data==null || data.toString().equals("")) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                data = formatter.format(new Date());
            }
        }
        return data;
    }
}
