package com.springboot.utils.thread;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-5-17 14:52
 */
@Service
public class AsyncService {

    @Async
    public Future<Integer> sum(int a, int b){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(a + b);
    }

    @Async("taskExecutor")
    public Future<Integer> sum2(int a, int b){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(a + b);
    }

    @Async
    public Future<Integer> sum(List<Integer> list){
        if(!CollectionUtils.isEmpty(list)){
            Integer sum = Math.toIntExact(list.stream().mapToInt(a -> a).summaryStatistics().getSum());
            return new AsyncResult<>(sum);
        }
        return null;
    }



}
