package com.lianyitech.modules.common;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.peri.entity.Binding;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by on 2016/10/29.
 */
public class ReportCommon {

    public static void totalcommon(List list, String[] attrs, Object setobj) throws Exception {
        if (list != null && list.size() > 0) {
            for (Object aList : list) {
                for (String attr : attrs) {//循环传入需要计算的实体类属性
                    Field field = aList.getClass().getDeclaredField(attr);
                    Field returnField = setobj.getClass().getDeclaredField(attr);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (!returnField.isAccessible()) {
                        returnField.setAccessible(true);
                    }
                    returnField.set(setobj, add(field.get(aList), returnField.get(setobj)));
                }
            }
        }
    }

    /**
     * 2个对象相除
     * @param Dividend 被除数
     * @param Divisor 除数
     * @return double
     */
    public static Double Comdivision(Number Dividend, Number Divisor) {
        BigDecimal b1 = new BigDecimal(Dividend != null ? Dividend.toString() : "0");
        BigDecimal b2 = new BigDecimal(Divisor != null ? Divisor.toString() : "0");
        if (new BigDecimal("0").compareTo(b2) < 0) {
            return b1.divide(b2, 2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else {
            return null;
        }
    }

    private static Object add(Object v1, Object v2) {
        BigDecimal b1 = new BigDecimal(v1!=null?v1.toString():"0");
        BigDecimal b2 = new BigDecimal(v2!=null?v2.toString():"0");
        if(v1 instanceof  Double || v2 instanceof  Double){
            return Double.parseDouble(b1.add(b2).toString());
        }
        if(v1 instanceof  Integer || v2 instanceof  Integer){
            return Integer.parseInt(b1.add(b2).toString());
        }
        return null;

    }

    /**
     * 索书号excel
     */
    public static String getIndexNum(Copy copy,String exportType) {
        StringBuilder indexNum = new StringBuilder("");
        switch (exportType) {
            case "1": //索书号导出excel：仅索书号1
                if (StringUtils.isEmpty(copy.getLibrarsortCode())) {
                    return "";
                } else {
                    indexNum.append(copy.getLibrarsortCode()).append("\n");
                }
                if (StringUtils.isNotEmpty(copy.getTanejiNo())) {
                    //indexNum.append("/").append(tanejiNo);
                    indexNum.append(copy.getTanejiNo());
                }
                if (StringUtils.isEmpty(copy.getTanejiNo())) {
                    //indexNum.append("/").append(bookNo);
                    indexNum.append(copy.getBookNo());
                }
                if (StringUtils.isNotEmpty(copy.getAssNo())) {
                    indexNum.append(":").append(copy.getAssNo());
                }
                break;
            case "2": //索书号导出excel：索书号+条形码
                if (StringUtils.isEmpty(copy.getLibrarsortCode())) {
                    return "";
                } else {
                    indexNum.append(copy.getLibrarsortCode());
                }
                if (StringUtils.isNotEmpty(copy.getTanejiNo())) {
                    indexNum.append("/").append(copy.getTanejiNo());
                }
                if (StringUtils.isEmpty(copy.getTanejiNo())) {
                    indexNum.append("/").append(copy.getBookNo());
                }
                if (StringUtils.isNotEmpty(copy.getAssNo())) {
                    indexNum.append(":").append(copy.getAssNo());
                }
                indexNum.append("\n").append(copy.getBarcode());
                break;
            case "3": //索书号导出excel：仅索书号2
                if (StringUtils.isEmpty(copy.getLibrarsortCode())) {
                    return "";
                } else {
                    indexNum.append(copy.getLibrarsortCode());
                }
                if (StringUtils.isNotEmpty(copy.getTanejiNo())) {
                    indexNum.append("/").append(copy.getTanejiNo());
                }
                if (StringUtils.isEmpty(copy.getTanejiNo())) {
                    indexNum.append("/").append(copy.getBookNo());
                }
                if (StringUtils.isNotEmpty(copy.getAssNo())) {
                    indexNum.append(":").append(copy.getAssNo());
                }
                break;
        }
        return indexNum.toString();
    }

    /**
     * 索刊号excel
     */
    public static String getSomNo(Binding binding, String exportType) {
        StringBuilder somNo = new StringBuilder("");
        switch (exportType) {
            case "1": //索刊号导出excel：仅索刊号1
                if (StringUtils.isEmpty(binding.getLibrarsortCode())) {
                    return "";
                } else {
                    somNo.append(binding.getLibrarsortCode()).append("\n");
                }
                if (StringUtils.isNotEmpty(binding.getBookTimeNo())) {
                    somNo.append(binding.getBookTimeNo());
                }
                if (StringUtils.isNotEmpty(binding.getAssNo())) {
                    somNo.append(":").append(binding.getAssNo());
                }
                break;
            case "2": //索刊号导出excel：索刊号+条形码
                if (StringUtils.isEmpty(binding.getLibrarsortCode())) {
                    return "";
                } else {
                    somNo.append(binding.getLibrarsortCode());
                }
                if (StringUtils.isNotEmpty(binding.getBookTimeNo())) {
                    somNo.append("/").append(binding.getBookTimeNo());
                }
                if (StringUtils.isNotEmpty(binding.getAssNo())) {
                    somNo.append(":").append(binding.getAssNo());
                }
                somNo.append("\n").append(binding.getBarcode());
                break;
            case "3": //索刊号导出excel：仅索刊号2
                if (StringUtils.isEmpty(binding.getLibrarsortCode())) {
                    return "";
                } else {
                    somNo.append(binding.getLibrarsortCode());
                }
                if (StringUtils.isNotEmpty(binding.getBookTimeNo())) {
                    somNo.append("/").append(binding.getBookTimeNo());
                }
                if (StringUtils.isNotEmpty(binding.getAssNo())) {
                    somNo.append(":").append(binding.getAssNo());
                }
                break;
        }
        return somNo.toString();
    }

    /**
     * 索书号txt
     */
    public static String getIndexNumTxt(Copy copy,String exportType) {
        StringBuilder indexNum = new StringBuilder("");
        if (StringUtils.isEmpty(copy.getLibrarsortCode())) {
            return "";
        } else {
            indexNum.append(copy.getLibrarsortCode());
        }
        if (StringUtils.isNotEmpty(copy.getTanejiNo())) {
            indexNum.append("/").append(copy.getTanejiNo());
        }
        if (StringUtils.isEmpty(copy.getTanejiNo())) {
            indexNum.append("/").append(copy.getBookNo());
        }
        if (StringUtils.isNotEmpty(copy.getAssNo())) {
            indexNum.append(":").append(copy.getAssNo());
        }
        switch (exportType) {
            case "1": //索书号导出txt：仅索书号
                break;
            case "2": //索书号导出txt：索书号+条形码1
                indexNum.append("/").append(copy.getBarcode());
                break;
            case "3": //索书号导出txt：索书号+条形码2
                indexNum.append("*").append(copy.getBarcode());
                break;
        }
        return indexNum.toString();
    }

    /**
     * 索刊号txt
     */
    public static String getSomNoTxt(Binding binding, String exportType) {
        StringBuilder somNo = new StringBuilder("");
        if (StringUtils.isEmpty(binding.getLibrarsortCode())) {
            return "";
        } else {
            somNo.append(binding.getLibrarsortCode());
        }
        if (StringUtils.isNotEmpty(binding.getBookTimeNo())) {
            somNo.append("/").append(binding.getBookTimeNo());
        }
        if (StringUtils.isNotEmpty(binding.getAssNo())) {
            somNo.append(":").append(binding.getAssNo());
        }
        switch (exportType) {
            case "1": //索刊号导出txt：仅索刊号
                break;
            case "2": //索刊号导出txt：索刊号+条形码1
                somNo.append("/").append(binding.getBarcode());
                break;
            case "3": //索刊号导出txt：索刊号+条形码2
                somNo.append("*").append(binding.getBarcode());
                break;
        }
        return somNo.toString();
    }

}
