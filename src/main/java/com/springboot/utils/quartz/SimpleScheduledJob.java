package com.springboot.utils.quartz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description: springboot2.0 自带的定时任务
 * @author: chenmingjian
 * @date: 19-1-25 15:37
 */

@Component
@Slf4j
public class SimpleScheduledJob {

    /**
     * 每五秒执行一次
     */
   // @Scheduled(fixedRate = 5000)
    private void startJob(){
        log.info("定时任务启动....");
    }
}
