package com.lianyitech.common.config;

import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

public class MVCConfigurer {
    public static ContentNegotiatingViewResolver contentViewResolver() {
        ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
        contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);
        contentNegotiationManager.addMediaType("xml", MediaType.APPLICATION_XML);
        contentNegotiationManager.setIgnoreAcceptHeader(true);
        contentNegotiationManager.setFavorPathExtension(true);
        ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
        contentViewResolver.setContentNegotiationManager(new ContentNegotiationManagerFactoryBean().getObject());
        MappingJackson2JsonView defaultView = new MappingJackson2JsonView();
        defaultView.setExtractValueFromSingleKeyModel(true);
        contentViewResolver.setDefaultViews(Collections.singletonList(defaultView));
        return contentViewResolver;
    }

    public static StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    public static MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.<MediaType>asList(MediaType.APPLICATION_JSON_UTF8));
        converter.setPrettyPrint(false);
        return converter;
    }

    public static SimpleMultipartResolver multipartResolver(int maxUploadSize, String excludeUrls) {
        SimpleMultipartResolver commonsMultipartResolver = new SimpleMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(maxUploadSize);//上传文件大小 50M 50*1024*1024
        commonsMultipartResolver.setExcludeUrls(excludeUrls);//此处拦截的URL不走Spring MultipartResolver
        return commonsMultipartResolver;
    }
}
