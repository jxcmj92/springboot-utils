package com.springboot.utils.thread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * @description: 线程池配置
 * @author: chenmingjian
 * @date: 19-5-17 14:32
 */

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolConfig implements AsyncConfigurer {

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

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);

        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * 对void方法抛出的异常处理
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }


    /**
     * 异常处理类
     */
    public static class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            log.error("错误信息：" + String.valueOf(ex.getStackTrace()));
        }
    }


}
