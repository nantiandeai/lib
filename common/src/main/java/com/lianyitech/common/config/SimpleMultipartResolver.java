package com.lianyitech.common.config;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * 解决Spring MultipartResolver和ServletFileUpload 冲突
 * Created by zengy on 2016/11/15.
 */

public class SimpleMultipartResolver extends CommonsMultipartResolver {
    private String excludeUrls;
    private String[] excludeUrlArray;

    public String getExcludeUrls() {
        return excludeUrls;
    }


    public void setExcludeUrls(String excludeUrls) {
        this.excludeUrls = excludeUrls;
        this.excludeUrlArray = excludeUrls.split(",");
    }


    /**
     * 这里是处理Multipart http的方法。如果这个返回值为true,那么Multipart http body就会MyMultipartResolver 消耗掉.如果这里返回false
     * 那么就会交给后面的自己写的处理函数处理例如刚才ServletFileUpload 所在的函数
     *
     * @see CommonsMultipartResolver#isMultipart(HttpServletRequest)
     */
    @Override
    public boolean isMultipart(HttpServletRequest request) {
        for (String url : excludeUrlArray) {
            if (request.getRequestURI().contains(url)) {
                return false;
            }
        }
        return super.isMultipart(request);
    }

}