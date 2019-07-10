package com.springboot.utils.thread;

import lombok.Data;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @description: 线程池配置
 * @author: chenmingjian
 * @date: 19-5-17 14:32
 */

@Data
@Component
@ConfigurationProperties(prefix = "thread.pool")
public class MultiThreadPoolConfig {

    /**
     * 核心线程数
     */
    private Integer corePoolSize;

    /**
     * 最大线程数
     */
    private Integer maxPoolSize;

    /**
     * 队列最大长度
     */
    private Integer queueCapacity;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    private Integer keepAliveSeconds;

    @Bean("taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        return taskExecutor;
    }

}
