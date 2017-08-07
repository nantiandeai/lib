package com.lianyitech.core.config;

import com.lianyitech.common.config.*;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisPool;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;

import java.sql.SQLException;

import static com.lianyitech.core.config.ConfigurerConstants.*;
import static com.lianyitech.core.config.ConfigurerConstants.JDBC_MAX_ACTIVE;
import static com.lianyitech.core.config.ConfigurerConstants.JDBC_MIN_IDLE;

/**
 * SpringContext Config
 */
@Configuration
@EnableTransactionManagement
public class ContextConfigurer {

    @Autowired
    private AbstractEnvironment environment;

    /**
     * 数据源配置
     * @return 数据源
     */
    @Bean
    public DataSource dataSource() throws SQLException {
        LoggerFactory.getLogger("ContextConfigurer").info(String.valueOf(environment.getSystemProperties()));
//        return DataSourceConfigurer.hikariDataSource(
//                environment.getProperty(JDBC_URL),
//                environment.getProperty(JDBC_USERNAME),
//                environment.getProperty(JDBC_PASSWORD),
//                environment.getProperty(JDBC_DRIVER),
//                Integer.valueOf(environment.getProperty(JDBC_CACHE_SIZE)),
//                Integer.valueOf(environment.getProperty(JDBC_CACHE_LIMIT))
//        );
        return DataSourceConfigurer.druidDataSource(
                environment.getProperty(JDBC_URL),
                environment.getProperty(JDBC_USERNAME),
                environment.getProperty(JDBC_PASSWORD),
                environment.getProperty(JDBC_DRIVER),
                Integer.valueOf(environment.getProperty(JDBC_CACHE_SIZE)),
                Integer.valueOf(environment.getProperty(JDBC_CACHE_LIMIT)),
                Integer.valueOf(environment.getProperty(JDBC_MIN_IDLE)),
                Integer.valueOf(environment.getProperty(JDBC_MAX_ACTIVE))
        );
    }



    /**
     * JedisConnectionFactory 配置
     * @return JedisConnectionFactory
     */
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(RedisConfigurer.jedisPoolConfig());
        redisConnectionFactory.setHostName(environment.getProperty(REDIS_HOST));
        redisConnectionFactory.setPort(Integer.valueOf(environment.getProperty(REDIS_PORT)));
        return redisConnectionFactory;
    }

    /**
     * RedisTemplate 配置
     * @param cf RedisConnectionFactory
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
    }

    /**
     * CacheManager 配置， SpringCache
     * @param redisTemplate RedisTemplate
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        // Number of seconds before expiration. Defaults to unlimited (0)
        cacheManager.setDefaultExpiration(3000); // Sets the default expire time (in seconds)
        return cacheManager;
    }

    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate sqlClient(DataSource dataSource) throws Exception {
        return new SqlSessionTemplate(MybatisConfigurer.sqlSessionFactoryBean(dataSource).getObject());
    }


    //-------------------------------------------------------------------------

    /**
     * 数据层验证
     * @return LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean LocalValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    //-------------------------------------------------------------------------

    /**
     * jedisPool 配置
     * @return jedisPool
     */
    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(
                RedisConfigurer.jedisPoolConfig(),
                environment.getProperty(REDIS_HOST),
                Integer.valueOf(environment.getProperty(REDIS_PORT))
        );
    }

    //-------------------------------------------------------------------------

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        return ClientHttpConfigurer.clientHttpRequestFactory();
    }

}