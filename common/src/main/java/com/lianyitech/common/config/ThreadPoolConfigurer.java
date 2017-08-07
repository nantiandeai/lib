package com.lianyitech.common.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


public class ThreadPoolConfigurer {
    public static ThreadPoolTaskExecutor threadPoolTaskExecutor(int corePoolSize, int maxPoolSize, int queueCapacity, int keepAliveSeconds) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);//核心线程数
        executor.setMaxPoolSize(maxPoolSize);//最大线程数
        executor.setQueueCapacity(queueCapacity);//队列最大长度
        executor.setKeepAliveSeconds(keepAliveSeconds);//线程池维护线程所允许的空闲时间,单位：s
        return executor;
    }
}
