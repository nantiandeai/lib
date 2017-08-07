package com.lianyitech.core.config;

import com.lianyitech.Proxy.CrawlerListener;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * 其它servlet和监听器等需要额外声明，用@Order注解设定启动顺序
 */
public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }

    @Override
    public void registerContextLoaderListener(ServletContext servletContext) {
        super.registerContextLoaderListener(servletContext);
        CrawlerListener crawlerListener = new CrawlerListener();
        servletContext.addListener(crawlerListener);
    }

    /**
     * 应用上下文，除web部分 加载配置文件类,如spring-*.xml，需要使用@Configuration注解进行标注
     */
    @Override
    protected Class[] getRootConfigClasses() {
        return new Class[]{ContextConfigurer.class};
    }

    /**
     * web上下文
     *
     * @return
     */
    @Override
    protected Class[] getServletConfigClasses() {
        return null;
    }


    /**
     * DispatcherServlet的映射路径
     *
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * 注册过滤器，映射路径与DispatcherServlet一致，
     * 路径不一致的过滤器需要注册到另外的WebApplicationInitializer中
     *
     * @return
     */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[]{characterEncodingFilter};
    }
}