package com.lianyitech.modules.common;

import org.apache.commons.beanutils.BeanUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by on 2016/10/29.
 */
public class ReportCommon{
    /**
     * 合计根据类属性求和
     * @param list 集合
     * @param attrs 属性参数数组
     * @param setobj 类
     * @throws Exception
     */
    public static void totalcommon(List list, String[] attrs, Object setobj) throws Exception {
        for (String attr : attrs) {//循环传入需要计算的实体类属性
            BeanUtils.setProperty(setobj, attr, total(list, attr));
        }
    }

    private static BigDecimal total(List list, String attr) throws Exception {
        BigDecimal b = new BigDecimal(0);
        for (Object aList : list) {//循环list
            Object o = BeanUtils.getProperty(aList, attr);
            if(o != null) {
                b = b.add(new BigDecimal(o.toString()));
            }
        }
        return b;
    }


    /**
     * 2个对象相除 保留2位小数点
     * @param Dividend 被除数
     * @param Divisor 除数
     * @return double
     */
    public static String comdivision(Number Dividend, Number Divisor) {
        BigDecimal b1 = new BigDecimal(Dividend != null ? Dividend.toString() : "0");
        BigDecimal b2 = new BigDecimal(Divisor != null ? Divisor.toString() : "0");
        if (new BigDecimal("0").compareTo(b2) < 0) {
            return b1.divide(b2, 2, BigDecimal.ROUND_HALF_EVEN).toString();
        } else {
            return "0.00";
        }
    }
}
