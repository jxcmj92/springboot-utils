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
public class StudentExcelProperty extends BaseRowModel {

    @ExcelProperty(index = 0)
    private String name;

    @ExcelProperty(index = 1)
    private String age;

    @ExcelProperty(index = 2)
    private String school;
}
