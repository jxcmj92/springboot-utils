package com.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description: 启动类
 * @Auther: chenmingjian
 * @Date: 18-11-7 15:12
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
@EnableAsync
public class Application {
    public static void main(String[] args) {
        log.info("项目启动开始....");
        SpringApplication.run(Application.class,args);
        log.info("项目启动结束....");
    }
}
