package com.lianyitech.common.config;

import redis.clients.jedis.JedisPoolConfig;

public class RedisConfigurer {

    public static JedisPoolConfig jedisPoolConfig () {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(300);
        jedisPoolConfig.setMaxTotal(60000);
        jedisPoolConfig.setTestOnBorrow(true);
        return jedisPoolConfig;
    }
}
