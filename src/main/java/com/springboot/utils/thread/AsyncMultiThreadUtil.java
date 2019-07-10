package com.springboot.utils.thread;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-5-17 16:56
 */

public class AsyncMultiThreadUtil {

    /**
     * 将集合切割成n等分
     */
    public static <T> List<List<T>> getMultiSubListFrom(List<T> list, int threadCount){
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        threadCount = (threadCount != 0) ? threadCount : 1;

        int listStartIndex;
        int listEndIndex;
        int listSize = list.size();
        List<List<T>> subLists = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            listStartIndex = listSize / threadCount * i;
            listEndIndex = listSize / threadCount * (i + 1);
            //最后一段线程会 出现与其他线程不等的情况
            if (i == (threadCount - 1)) {
                listEndIndex = listSize;
            }
            List<T> subList = list.subList(listStartIndex, listEndIndex);
            subLists.add(subList);
        }

        return subLists;
    }

}
