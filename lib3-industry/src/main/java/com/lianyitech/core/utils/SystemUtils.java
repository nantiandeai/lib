/**
 *
 */
package com.lianyitech.core.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 业务系统工具类
 *
 * @version 2016-09-12
 */
public class SystemUtils {

    private static Logger logger = LoggerFactory.getLogger(SystemUtils.class);

    /**
     * map转实体类
     *
     * @param map
     * @param obj
     */
    public static void transMapToBean(Map<String, Object> map, Object obj) {
        if (map == null || obj == null) {
            return;
        }
        try {
            BeanUtils.populate(obj, map);
        } catch (Exception e) {
            logger.debug("transMapToBean Error " + e);
        }
    }
}