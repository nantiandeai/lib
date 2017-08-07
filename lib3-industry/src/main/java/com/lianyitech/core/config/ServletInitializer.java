package com.lianyitech.core.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.lianyitech.common.config.FilterConfiguer;
import com.lianyitech.common.servlet.DownloadServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.HashMap;
import java.util.Map;

/**
 * MVC相关配置，比如加载spring配置文件，声明DispatcherServlet等等
 * 其它servlet和监听器等需要额外声明，用@Order注解设定启动顺序
 */
public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        //注册downloadServlet
        servletContext
                .addServlet("downloadServlet", DownloadServlet.class)
                .addMapping("/uploadfiles/*");

        //注册statViewServlet
        Map<String,String> druidMap=new HashMap<>();
        druidMap.put("loginUsername","druid-data");
        druidMap.put("loginPassword","druid-data");
        ServletRegistration servletRegistration=servletContext.addServlet("statViewServlet", StatViewServlet.class);
        servletRegistration.setInitParameters(druidMap);
        servletRegistration.addMapping("/druid/*");
    }

    /*
      * 应用上下文，除web部分
      * 加载配置文件类,如spring-*.xml，需要使用@Configuration注解进行标注
      */
    @Override
    protected Class[] getRootConfigClasses() {
        return new Class[]{WebMvcConfigurer.class};
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
        return FilterConfiguer.filters();
    }

}