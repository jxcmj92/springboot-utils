package com.springboot.utils.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 测试类
 * @author: chenmingjian
 * @date: 19-4-4 15:24
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EasyExcelTest {

    /**
     * 读取少于1000行的excel
     */
    @org.junit.Test
    public void readLessThan1000Row() throws IOException {
        String filePath = "/home/chenmingjian/Downloads/新建+XLSX+工作表.xlsx";
        List<Object> objects = EasyExcelUtil.readLessThan1000Row(filePath);
        objects.forEach(System.out::println);
    }

    /**
     * 读取少于1000行的excle，可以指定sheet和从几行读起
     */
    @org.junit.Test
    public void readLessThan1000RowBySheet() throws IOException {
        String filePath = "/home/chenmingjian/Downloads/测试.xlsx";
        //表示从sheet1表格中，从第二行开始读取
        Sheet sheet = new Sheet(1, 1);
        List<Object> objects = EasyExcelUtil.readLessThan1000Row(filePath,sheet);

        //也可以这样表示： 从sheet1表格中，从第二行开始读取
       // List<Object> objects = ExcelUtil.readLessThan1000Row(filePath,ExcelUtil.NON_HEADER_SHEET);
        objects.forEach(System.out::println);
    }

    /**
     * 读取大于1000行的excel
     * 带sheet参数的方法可参照测试方法readLessThan1000RowBySheet()
     */
    @org.junit.Test
    public void readMoreThan1000Row() throws IOException {
        String filePath = "/home/chenmingjian/Downloads/新建+XLSX+工作表.xlsx";
        List<List<String>> objects = EasyExcelUtil.readMoreThan1000Row(filePath);
        objects.forEach(System.out::println);
    }


    /**
     * 生成excel
     * 带sheet参数的方法可参照测试方法readLessThan1000RowBySheet()
     */
    @org.junit.Test
    public void writeBySimple() throws IOException {
        String filePath = "/home/chenmingjian/Downloads/测试.xlsx";
        List<List<Object>> data = new ArrayList<>();
        data.add(Arrays.asList("111","222","333"));
        data.add(Arrays.asList("111","222","333"));
        data.add(Arrays.asList("111","222","333"));
        List<String> head = Arrays.asList("表头1", "表头2", "表头3");
        EasyExcelUtil.writeToFilePath(filePath,data,head);
    }


    /**
     * 生成excel, 带用模型
     * 带sheet参数的方法可参照测试方法readLessThan1000RowBySheet()
     */
    @org.junit.Test
    public void writeWithTemplate() throws IOException {
        String filePath = "/home/chenmingjian/Downloads/测试.xlsx";
        ArrayList<TableHeaderExcelProperty> data = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            TableHeaderExcelProperty tableHeaderExcelProperty = new TableHeaderExcelProperty();
            tableHeaderExcelProperty.setName("cmj" + i);
            tableHeaderExcelProperty.setAge(22 + i);
            tableHeaderExcelProperty.setSchool("清华大学" + i);
            data.add(tableHeaderExcelProperty);
        }
        EasyExcelUtil.writeWithTemplateToFilePath(filePath,data);
    }


    /**
     * 生成excel, 带用模型,带多个sheet
     */
    @org.junit.Test
    public void writeWithMultipleSheet() throws IOException {
        ArrayList<EasyExcelUtil.MultipleSheetProperty> list1 = new ArrayList<>();
        for(int j = 1; j < 4; j++){
            ArrayList<TableHeaderExcelProperty> list = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                TableHeaderExcelProperty tableHeaderExcelProperty = new TableHeaderExcelProperty();
                tableHeaderExcelProperty.setName("cmj" + i);
                tableHeaderExcelProperty.setAge(22 + i);
                tableHeaderExcelProperty.setSchool("清华大学" + i);
                list.add(tableHeaderExcelProperty);
            }

            Sheet sheet = new Sheet(j, 0);
            sheet.setSheetName("sheet" + j);

            EasyExcelUtil.MultipleSheetProperty multipleSheetProperty = new EasyExcelUtil.MultipleSheetProperty();
            multipleSheetProperty.setData(list);
            multipleSheetProperty.setSheet(sheet);

            list1.add(multipleSheetProperty);

        }

        EasyExcelUtil.writeWithMultipleSheet("/home/chenmingjian/Downloads/aaa.xlsx",list1);

    }


    /*******************匿名内部类，实际开发中该对象要提取出去**********************/


    /**
     * @description:
     * @author: chenmingjian
     * @date: 19-4-3 14:44
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class TableHeaderExcelProperty extends BaseRowModel {

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

    /*******************匿名内部类，实际开发中该对象要提取出去**********************/

}
