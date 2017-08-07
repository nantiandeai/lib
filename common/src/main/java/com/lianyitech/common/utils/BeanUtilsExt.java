package com.lianyitech.common.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 自定义BeanUtils，扩展日期转换
 * Created by zengyuanmei on 2016/10/21.
 */
public class BeanUtilsExt extends BeanUtils {


    private BeanUtilsExt() {
    }

//    static {
//        // 注册sql.date的转换器，即允许BeanUtils.copyProperties时的源目标的sql类型的值允许为空
//        ConvertUtils.register(new org.apache.commons.beanutils.converters.SqlDateConverter(null), java.sql.Date.class);
//        ConvertUtils.register(new org.apache.commons.beanutils.converters.SqlDateConverter(null), java.util.Date.class);
//        ConvertUtils.register(new org.apache.commons.beanutils.converters.SqlTimestampConverter(null), java.sql.Timestamp.class);
//        // 注册util.date的转换器，即允许BeanUtils.copyProperties时的源目标的util类型的值允许为空
//    }

    public static void copyProperties(Object target, Object source)
            throws InvocationTargetException, IllegalAccessException {
        // 注册sql.date的转换器，即允许BeanUtils.copyProperties时的源目标的sql类型的值允许为空
        ConvertUtils.register(new org.apache.commons.beanutils.converters.SqlDateConverter(null), java.sql.Date.class);
        ConvertUtils.register(new org.apache.commons.beanutils.converters.SqlDateConverter(null), java.util.Date.class);
        ConvertUtils.register(new org.apache.commons.beanutils.converters.SqlTimestampConverter(null), java.sql.Timestamp.class);

        // 支持对日期copy
        org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
    }
}
