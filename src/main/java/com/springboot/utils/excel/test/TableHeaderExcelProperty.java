package com.springboot.utils.excel.test;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-4-3 14:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TableHeaderExcelProperty extends BaseRowModel {

    /**
     * value: 表头名称
     * index: 列的号, 0表示第一列
     */
    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "年龄",index = 1)
    private int age;

    @ExcelProperty(value = "学校",index = 2)
    private String school;
}
