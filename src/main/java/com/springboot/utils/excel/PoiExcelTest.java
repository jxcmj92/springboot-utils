package com.springboot.utils.excel;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PoiExcelTest {

    @org.junit.Test
    public void read() {
        List<List<String>> lists = PoiExcelUtil.readExcel("C:\\新建文件夹\\template (1).xlsx");
        System.out.println(lists);
    }

    @org.junit.Test
    public void write() {
        List<List<String>> lists = PoiExcelUtil.readExcel("C:\\新建文件夹\\template (1).xlsx");
        System.out.println(lists);
    }

}
