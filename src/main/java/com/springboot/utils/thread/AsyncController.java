package com.springboot.utils.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-5-17 14:53
 */
@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/asyncTest")
    public void test() throws ExecutionException, InterruptedException {

        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        ArrayList<Future<Integer>> futures = new ArrayList<>();
        List<List<Integer>> multiSubList = AsyncMultiThreadUtil.getMultiSubListFrom(list,4);
        if(!CollectionUtils.isEmpty(multiSubList)){
            multiSubList.forEach(sublist -> futures.add(asyncService.sum(sublist)));
        }


        AtomicInteger k = new AtomicInteger();
        futures.forEach(future -> {
            try {
                System.out.println(future.get());
                k.addAndGet(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

    }
}