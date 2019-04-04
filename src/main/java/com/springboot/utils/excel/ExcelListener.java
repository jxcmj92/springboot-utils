package com.springboot.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析监听器，
 * 每解析一行会回调invoke()方法。
 * 整个excel解析结束会执行doAfterAllAnalysed()方法
 *
 * @author: chenmingjian
 * @date: 19-4-3 14:11
 */
@Getter
@Setter
public class ExcelListener extends AnalysisEventListener {

    private List<Object> datas = new ArrayList<>();


    /**
     * 逐行解析
     * object : 当前行的数据
     */
    @Override
    public void invoke(Object object, AnalysisContext context) {
        //当前行
        // context.getCurrentRowNum()
        if(object != null){
            datas.add(object);
        }
    }


    /**
     *  解析完所有数据后会调用该方法
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //解析结束销毁不用的资源
    }

}
