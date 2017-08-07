package com.lianyitech.core.utils;

import com.lianyitech.common.utils.DoubleUtils;
import com.lianyitech.common.utils.SpringContextHolder;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.catalog.entity.BookDirectoryForImport;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.lianyitech.core.config.ConfigurerConstants.IMPORT_ENABLE;

/**
 * Created by Administrator on 2017/5/19.
 */
public class CommonUtils {
    private static AbstractEnvironment environment = SpringContextHolder.getBean(AbstractEnvironment.class);

    public static void setCvsConfig(HttpServletResponse response,String fileName){
        response.setContentType("application/csv;charset=gbk");
        String codedFileName = "";
        try {
            codedFileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {

        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName +  ".csv");
        response.setBufferSize(2048);
    }


    public static String parseDouble(Double d){
        return DoubleUtils.formatDoubleToString(d);
    }

    public static String parseDouble(String s){
        Double d = Double.valueOf(s);
        return DoubleUtils.formatDoubleToString(d);
    }

    public static boolean isImportServer(){
        boolean flag = false;
        String importEnable = environment.getProperty(IMPORT_ENABLE);
        if(StringUtils.isNotBlank(importEnable) && importEnable.equals("1")) {
            flag  = true ;
        }
        return flag ;
    }

    /**
     * 导入日期处理
     * 1、把所有非正常字符转换为“-”
     * 2、截取前4位，判断是否合法年份
     * 3、对于包含“-”的，截取二段做月份处理
     * 4、不包含“-”的，截取后两位做月份处理
     * 5、月份补全或者不合法的，补全“01”
     * @param bookDirectory
     * @return
     */
    public static boolean resolveImportDate(BookDirectoryForImport bookDirectory) {
        String resultDate = "";
        String publishDate = bookDirectory.getPublishingTime();
        if (StringUtils.isNotBlank(publishDate)) {
            publishDate = publishDate.trim();
            publishDate = publishDate.replaceAll("，","");
            publishDate = publishDate.replaceAll("—","-");
            publishDate = publishDate.replaceAll("。","");
            publishDate = publishDate.replaceAll(",","");
            publishDate = publishDate.replaceAll("、","");
            publishDate = publishDate.replaceAll("年","");
            publishDate = publishDate.replaceAll("月","");
            publishDate = publishDate.replaceAll("\\.","");
            publishDate = publishDate.replaceAll("\\.","");
            publishDate = publishDate.replaceAll("/","");
            String year = "";
            String month = "";
            if(publishDate.contains("-")) {
                year = publishDate.substring(0,publishDate.indexOf("-")).trim();
                month = publishDate.substring(publishDate.indexOf("-")+1);
            } else if(publishDate.length()>=4) {
                year = publishDate.substring(0,4);
                month = publishDate.substring(4);
            }

            if(year.matches("^\\d{4}$") && (year.startsWith("1") || year.startsWith("2") )){
                resultDate = year + "-" ;
            } else {
                bookDirectory.setErrorinfo("日期格式不符合规范");
                return false;
            }
            resultDate = resultDate + parseMonth(month) ;
            bookDirectory.setPublishingTime(resultDate);
         }
        return true;
    }

    /**
     * 解析月份
     * @param month
     * @return
     */
    public static String parseMonth(String month){
        String result = "01";
        if(StringUtils.isNotBlank(month)) {
            month = month.substring(0,month.indexOf("-")==-1?month.length():month.indexOf("-"));
            if(month.length()==1) {
                month = "0" + month;
            } else {
                month = month.substring(0,2);
            }
            if(month.matches("^\\d{2}$")) {
                String a = month.substring(0,1);
                String b = month.substring(1,2);
                if(Integer.valueOf(a)>1 || (Integer.valueOf(a) == 1 && Integer.valueOf(b)>2) || Integer.valueOf(b)==0) {
                    result = "01";
                }  else {
                    result = month ;
                }
            }
        }
        return result;
    }



    public static boolean checkIsNotNull(Map<String,String> map) {
        if(map==null) {
            return false ;
        }
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            if(StringUtils.isNotBlank(stringStringEntry.getValue())) {
                return true;
            }
        }
        return false ;
    }


    public static boolean checkIsNotNull(List<String> list){
        if(list==null) {
            return false ;
        }
        for (String s : list) {
            if(StringUtils.isNotEmpty(s)) {
                return true;
            }
        }
        return false ;
    }



    /**
     * 书目导入长度校验，如果所有字段校验通过则返回true,如果不通过返回false并写入error
     * @param bookDirectoryForImport
     * @return
     */
    public static boolean checkLength (BookDirectoryForImport bookDirectoryForImport) {
        boolean flag = true ;
        StringBuffer errorStr = new StringBuffer();
        if(!checkLength(bookDirectoryForImport.getTitle(),450)) {
            flag = false ;
            errorStr.append("题名太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getSubTitle(),450)) {
            flag = false ;
            errorStr.append("副题名太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getAuthor(),450)) {
            flag = false ;
            errorStr.append("著者号太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getSubAuthor(),450)) {
            flag = false ;
            errorStr.append("次要责任者太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getEdition(),450)) {
            flag = false ;
            errorStr.append("版次太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getPublishingAddress(),450)) {
            flag = false ;
            errorStr.append("出版地太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getPublishingName(),450)) {
            flag = false ;
            errorStr.append("出版社太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getLibrarsortCode(),450)) {
            flag = false ;
            errorStr.append("分类号太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getTanejiNo(),450)) {
            flag = false ;
            errorStr.append("种次号太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getAssNo(),450)) {
            flag = false ;
            errorStr.append("辅助区分号太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getMeasure(),450)) {
            flag = false ;
            errorStr.append("尺寸太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getPageNo(),450)) {
            flag = false ;
            errorStr.append("页码太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getBindingForm(),450)) {
            flag = false ;
            errorStr.append("装帧形式太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getBestAge(),450)) {
            flag = false ;
            errorStr.append("适读年龄太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getPurpose(),60)) {
            flag = false ;
            errorStr.append("图书用途太长，不能超过20个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getTiedTitle(),450)) {
            flag = false ;
            errorStr.append("并列题名太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getPartName(),450)) {
            flag = false ;
            errorStr.append("分辑名太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getPartNum(),450)) {
            flag = false ;
            errorStr.append("分辑号太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getSeriesName(),450)) {
            flag = false ;
            errorStr.append("丛编名太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getSeriesEditor(),450)) {
            flag = false ;
            errorStr.append("丛编编者太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getTranslator(),450)) {
            flag = false ;
            errorStr.append("译者太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getSubject(),450)) {
            flag = false ;
            errorStr.append("主题词太长，不能超过150个字符；");
        }
        if(!checkLength(bookDirectoryForImport.getLanguage(),450)) {
            flag = false ;
            errorStr.append("语种太长，不能超过150个字符；");
        }

        if(!checkLength(bookDirectoryForImport.getAttachmentNote(),450)) {
            flag = false ;
            errorStr.append("附件备注太长，不能超过150个字符；");
        }

        if(!checkLength(bookDirectoryForImport.getBatchNo(),20)) {
            flag = false ;
            errorStr.append("批次号太长，不能超过20个字符；");
        }

        if(!checkLength(bookDirectoryForImport.getCollectionSiteName(),90)) {
            flag = false ;
            errorStr.append("馆藏地太长，不能超过30个字符；");
        }
        setErrorInfo(bookDirectoryForImport,errorStr.toString());
        return flag ;
    }


    public static void setErrorInfo(BookDirectoryForImport bookDirectoryForImport,String errorInfo){
        String str = bookDirectoryForImport.getErrorinfo();
        if(StringUtils.isBlank(str)) {
            str = "";
        }
        if(StringUtils.isNotBlank(errorInfo)) {
            str = str + errorInfo;
        }
        bookDirectoryForImport.setErrorinfo(str);
    }
    public static boolean checkLength(String str , int maxLen){
        if(StringUtils.isBlank(str)) {
            return true;
        }
        if(getLength(str)>maxLen) {
            return false;
        }
        return true;
    }


    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    public static int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }


    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
     * @param  s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int getLength(String s) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (isChinese(temp)) {
                // 中文字符长度为3
                valueLength += 3;
            } else {
                // 其他字符长度为0.5
                valueLength += 1;
            }
        }
        //进位取整
        return  valueLength;
    }

    public static void main(String[] args) {
        BookDirectoryForImport bookDirectoryForImport = new BookDirectoryForImport();
        bookDirectoryForImport.setBarcode("000006");
        System.out.println(checkLength(bookDirectoryForImport.getBarcode(),50));

//        BookDirectoryForImport dir = new BookDirectoryForImport();
//        dir.setPublishingTime("2006.12");
//        boolean result =  resolveImportDate(dir);
//        System.out.println(result + dir.getPublishingTime());
//        List<String> list = new ArrayList<>();
//        list.add("");
//        list.add("");
//        System.out.println(checkIsNotNull(list));
//
//        Map<String,String> map = new HashMap<>() ;
//        map.put("key1","");
//        map.put("key2","");
//        map.put("key3","");
//        System.out.println(checkIsNotNull(map));
    }
}
