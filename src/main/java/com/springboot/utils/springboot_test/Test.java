package com.springboot.utils.springboot_test;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.springboot.utils.excel.ExcelUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-7-1 11:20
 */
@RestController
public class Test {

    @GetMapping("/excel")
    public String test2(HttpServletResponse response) throws Exception {
        String name = "演示导出模板.xlsx";
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatDate_ymd = sdf_ymd.format(date);
        // 设置文件名
        String fileName = formatDate_ymd + name;
        String sheetName = "数据展示";

        // 按条件筛选records
        ArrayList<com.springboot.utils.excel.Test.TableHeaderExcelProperty> data = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            com.springboot.utils.excel.Test.TableHeaderExcelProperty tableHeaderExcelProperty = new com.springboot.utils.excel.Test.TableHeaderExcelProperty();
            tableHeaderExcelProperty.setName("cmj" + i);
            tableHeaderExcelProperty.setAge(22 + i);
            tableHeaderExcelProperty.setSchool("清华大学88888888888888" + i);
            data.add(tableHeaderExcelProperty);
        }

        ExcelUtil.writeWithTemplateToBrowser(data,response);

        return "success";
    }

    public static void writeExcel(HttpServletResponse response, List<? extends BaseRowModel> list,
                                  String fileName, String sheetName, BaseRowModel model)throws Exception  {
        ExcelWriter writer = new ExcelWriter(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, model.getClass());
        sheet.setSheetName(sheetName);
        writer.write(list, sheet);
        writer.finish();
    }

    @PostMapping("/post/test")
    public JSONObject test(Object object){
        System.out.println(object);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status","success");
        return jsonObject;
    }

    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName );
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            throw new Exception("导出excel表格失败!", e);
        }
    }

}
