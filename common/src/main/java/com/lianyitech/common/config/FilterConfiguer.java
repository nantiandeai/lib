package com.lianyitech.common.config;

import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;

/**
 * Created by tangwei on 2017/4/5.
 */
public class FilterConfiguer {
    public static Filter[] filters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        WebStatFilter druidWebStatFilter = new WebStatFilter();
        return new Filter[]{characterEncodingFilter, druidWebStatFilter};
    }
}
