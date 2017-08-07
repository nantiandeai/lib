package com.lianyitech.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lianyitech.common.config.*;
import com.lianyitech.modules.catalog.utils.ImportConsumeMq;
import com.rabbitmq.client.Channel;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.SQLException;
import java.util.Properties;

import static com.lianyitech.core.config.ConfigurerConstants.*;
import static com.lianyitech.core.utils.CommonUtils.isImportServer;
import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;

/**
 * SpringContext Config
 */
@Configuration
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@EnableCaching
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
     * 事务管理配置
     * @param dataSource 数据源 自动注入
     * @return 事务管理
     */
    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * mybatis client配置
     * @param dataSource 数据源 自动注入
     * @return SqlSessionTemplate
     * @throws Exception e
     */
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
     * solr 服务器配置(书目)
     * @return HttpSolrServer
     */
    @Bean
    public HttpSolrServer bookSolrServer() {
        return SolrServerConfigurer.solrServer(
                environment.getProperty(BOOK_SOLR_URL) + "/solr/book",
                Integer.valueOf(environment.getProperty(SOLR_MAX_RETRIES)),
                Integer.valueOf(environment.getProperty(SOLR_CONNECTION_TIMEOUT))
        );
    }

    /**
     * solr 服务器配置(期刊)
     * @return HttpSolrServer
     */
    @Bean
    public HttpSolrServer periSolrServer() {
        return SolrServerConfigurer.solrServer(
                environment.getProperty(SOLR_URL)  + "/solr/peri",
                Integer.valueOf(environment.getProperty(SOLR_MAX_RETRIES)),
                Integer.valueOf(environment.getProperty(SOLR_CONNECTION_TIMEOUT))
        );
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
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        return ClientHttpConfigurer.clientHttpRequestFactory();
    }

    //-------------------------------------------------------------------------

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        return ThreadPoolConfigurer.threadPoolTaskExecutor(5, 20, 1000, 60);
    }

    //<<RabbitMQ-------------------------------------------------------------------------
    @Bean
    public ConnectionFactory connectionFactory() {
        return MQConfigurer.connectionFactory(
                environment.getProperty(RABBIT_HOST),
                Integer.valueOf(environment.getProperty(RABBIT_PORT)),
                environment.getProperty(RABBIT_USERNAME),
                environment.getProperty(RABBIT_PASSWORD)
        );
    }
    @Bean
    public com.rabbitmq.client.ConnectionFactory getConnectionFactory(){
        com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
        factory.setHost(environment.getProperty(RABBIT_HOST));
        factory.setPort(Integer.valueOf(environment.getProperty(RABBIT_PORT)));
        factory.setUsername( environment.getProperty(RABBIT_USERNAME));
        factory.setPassword(environment.getProperty(RABBIT_PASSWORD));
        return factory;
    }
    public static final String BOOK_DIRECTORY_EXCHANGE_NAME = "direct.lib3.book.directory.exchange";
    public static final String BOOK_DIRECTORY_QUEUE_NAME = "direct.lib3.book.directory.queue";
    public static final String IMPORT_QUEUE_NAME = "direct.lib3.import";
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @RabbitListener(bindings = @QueueBinding(
            value = @org.springframework.amqp.rabbit.annotation.Queue(BOOK_DIRECTORY_QUEUE_NAME),
            exchange = @Exchange(value = BOOK_DIRECTORY_EXCHANGE_NAME, type = ExchangeTypes.TOPIC)))
    public @interface BookDirectoryListener { }

    @Bean
    public RabbitTemplate bookDirectoryTemplate(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        //创建Exchange
        TopicExchange exchange = new TopicExchange(BOOK_DIRECTORY_EXCHANGE_NAME);
        admin.declareExchange(exchange);
        //创建Queue
        Queue queue = new Queue(BOOK_DIRECTORY_QUEUE_NAME);
        admin.declareQueue(queue);
        //创建Binding
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(BOOK_DIRECTORY_QUEUE_NAME));
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(MANUAL);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }





    //>>RabbitMQ-------------------------------------------------------------------------

}