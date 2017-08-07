package com.lianyitech.core.config;

import com.lianyitech.common.config.RedisConfigurer;
import com.lianyitech.common.config.ThreadPoolConfigurer;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import static com.lianyitech.core.config.ConfigurerConstants.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * SpringContext Config
 */
@Configuration
@EnableTransactionManagement
@EnableCaching
@EnableRabbit
@ComponentScan(basePackages = "com.lianyitech")
@PropertySources({
        @PropertySource(value = "classpath:project.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file://${config.root}/${config.area}/${config.file}", ignoreResourceNotFound = true)
})
public class ContextConfigurer {

    @Autowired
    private Environment environment;

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

    //-------------------------------------------------------------------------

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        return ThreadPoolConfigurer.threadPoolTaskExecutor(5, 20, 1000, 60);
    }

    @Bean
    public LocalValidatorFactoryBean LocalValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

}