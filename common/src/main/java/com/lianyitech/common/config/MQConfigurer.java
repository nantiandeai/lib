package com.lianyitech.common.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

/**
 * Created by tangwei on 2017/2/21.
 */
public class MQConfigurer {
    public static CachingConnectionFactory connectionFactory(String host, int port, String userName, String password) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

}
