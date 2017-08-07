package com.lianyitech.common.utils;

import com.lianyitech.common.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengy on 2016/12/2.
 */
public class Base64Utils {

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBase64(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        key = key.replace("-", "/");  //解决URL参数带/问题。
        byte[] byteArray = Base64.decodeBase64(key);
        String code = new String(byteArray);
        return code;

    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBase64(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        byte[] binaryData = key.getBytes();
        String code = Base64.encodeBase64String(binaryData);
        return code.replace("/", "-");//解决URL参数带/问题。
    }

    /**
     * 将 String 转为 map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (String pv : params) {
            String[] p = pv.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

}
