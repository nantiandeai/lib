package com.lianyitech.core.config;

import com.lianyitech.common.config.*;
import com.lianyitech.common.config.SimpleMultipartResolver;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.core.interceptor.SubmitTokenInterceptor;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * SpringMvc Config
 */
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = "com.lianyitech")
@PropertySources({
        @PropertySource(value = "classpath:project.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file://${config.root}/${config.area}/${config.file}", ignoreResourceNotFound = false)
})
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("authorization", "content-type")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins("*");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 多个拦截器组成一个拦截器链
     * addPathPatterns 用于添加拦截规则
     * excludePathPatterns 用户排除拦截
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SubmitTokenInterceptor()).addPathPatterns("/api/**");
        super.addInterceptors(registry);
    }

    /**
     * 以下两个方法是Spring mvc内容协商机制的配置
     *
     * @return
     */
    @Bean
    public ViewResolver viewResolver() {
        return MVCConfigurer.contentViewResolver();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(MVCConfigurer.stringHttpMessageConverter());
        converters.add(MVCConfigurer.mappingJackson2HttpMessageConverter());
    }

    /**
     * MyBatis 扫描配置，这个配置影响properties文件加载，所以放到了这个位置
     *
     * @return MapperScannerConfigurer
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer c = new MapperScannerConfigurer();
        c.setBasePackage("com.lianyitech.modules.**.dao");
        c.setAnnotationClass(MyBatisDao.class);
        return c;
    }

    @Bean
    public SimpleMultipartResolver multipartResolver() {
        return MVCConfigurer.multipartResolver(50 * 1024 * 1024, "pub/uploadFile");
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lianyitech.modules"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("图书3.0学校端")
                .termsOfServiceUrl("")
                .version("1.1")
                .build();
    }


}