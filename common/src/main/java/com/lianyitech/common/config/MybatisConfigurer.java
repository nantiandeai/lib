package com.lianyitech.common.config;

import com.lianyitech.common.persistence.BaseEntity;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

public class MybatisConfigurer {

    public static SqlSessionFactoryBean sqlSessionFactoryBean (DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        try {
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean.setTypeAliasesPackage("com.lianyitech");
            sqlSessionFactoryBean.setTypeAliasesSuperType(BaseEntity.class);
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mappings/**/*.xml"));
            sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:/mybatis-config.xml"));
        } catch (IOException ignored) { }
        return sqlSessionFactoryBean;
    }
}
