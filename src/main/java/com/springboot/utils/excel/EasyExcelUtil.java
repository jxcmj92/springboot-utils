package com.springboot.utils.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: 阿里easyexcel工具类二次封装
 * @author: chenmingjian
 * @date: 19-3-18 16:16
 */
@Slf4j
public class EasyExcelUtil {


    /**
     * 初始化Sheet
     */
    private static Sheet initSheet(){
        Sheet initSheet = new Sheet(1, 0);
        initSheet.setSheetName("sheet1");
        //设置自适应宽度
        initSheet.setAutoWidth(Boolean.TRUE);

        return initSheet;
    }

    /**
     * 从第二行开始读取
     */
    public static Sheet getNonHeaderSheet(){
        Sheet initSheet = new Sheet(1, 1);
        initSheet.setSheetName("sheet1");
        //设置自适应宽度
        initSheet.setAutoWidth(Boolean.TRUE);

        return initSheet;
    }


    /**
     * 从文件路径中读取少于1000行数据
     * @param filePath 文件绝对路径
     */
    public static <T> List<T> readLessThan1000Row(String filePath) throws IOException {
        return readLessThan1000Row(filePath,null);
    }

    /**
     * 从文件路径中读小于1000行数据, 带样式
     * filePath 文件绝对路径
     * Sheet ：
     *      sheetNo: sheet页码，默认为1
     *      headLineMun: 从第几行开始读取数据，默认为0, 表示从第一行开始读取
     *      clazz: 返回数据List<Object> 中Object的类名
     */
    public static <T> List<T> readLessThan1000Row(String filePath, Sheet sheet) throws IOException {
        InputStream fileStream = new FileInputStream(filePath);
        return readLessThan1000Row(fileStream,sheet);
    }

    /**
     * 读取小于1000行数据
     * @param inputStream excel输入流
     */
    public static <T> List<T> readLessThan1000Row(InputStream inputStream){
        return readLessThan1000Row(inputStream,null);
    }

    /**
     * 读取小于1000行数据，带样式
     * @param inputStream excel输入流
     * @param sheet excel样式
     */
    public static <T> List<T> readLessThan1000Row(InputStream inputStream, Sheet sheet) {
        try {
            if(inputStream != null){
                BufferedInputStream fileStream = new BufferedInputStream(inputStream);
                sheet = (sheet != null) ? sheet : initSheet();
                List<Object> data = EasyExcelFactory.read(fileStream,sheet);
                return (List<T>) data;
            }
        }catch (Exception e){
            log.error("excel读取失败");
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("excel读取失败");
            }
        }

        return Collections.emptyList();
    }

    /**
     * 读大于1000行数据
     * @param filePath 文件绝对路径
     */
    public static <T> List<T> readMoreThan1000Row(String filePath) throws IOException {
        return readMoreThan1000Row(filePath,null);
    }

    /**
     * 读大于1000行数据, 带样式
     * @param filePath 文件绝对路径
     */
    public static <T> List<T> readMoreThan1000Row(String filePath, Sheet sheet) throws IOException {
        InputStream fileStream = new FileInputStream(filePath);
        return readMoreThan1000Row(fileStream,sheet);
    }

    /**
     * 读大于1000行数据
     */
    public static <T> List<T> readMoreThan1000Row(InputStream inputStream) throws IOException {
        return readMoreThan1000Row(inputStream,null);
    }

    /**
     * 读大于1000行数据
     */
    public static <T> List<T> readMoreThan1000Row(InputStream inputStream, Sheet sheet) throws IOException {
        BufferedInputStream fileStream = new BufferedInputStream(inputStream);
        sheet = (sheet != null) ? sheet : initSheet();
        ExcelListener<T> excelListener = new ExcelListener<>();
        EasyExcelFactory.readBySax(fileStream, sheet, excelListener);
        List<T> data = excelListener.getData();
        inputStream.close();
        return data;
    }


    /**
     * 导出excel到指定文件
     * @param filePath  绝对路径, 如：/home/chenmingjian/Downloads/aaa.xlsx
     * @param data 数据源
     * @param head 表头
     */
    public static void writeToFilePath(String filePath, List<List<Object>> data, List<String> head) throws IOException {
        writeToFilePath(filePath,data,head,null);
    }


    /**
     * 导出excel到指定文件
     * @param filePath 绝对路径, 如：/home/chenmingjian/Downloads/aaa.xlsx
     * @param data 数据源, 里面的list表示一行的数据，外面的list表示每行的数据
     * @param sheet excel页面样式
     * @param header 表头
     */
    public static void writeToFilePath(String filePath, List<List<Object>> data, List<String> header, Sheet sheet) throws IOException {
        OutputStream outputStream = new FileOutputStream(filePath);
        write(outputStream,data,header,sheet);
    }

    /**
     * 通过浏览器导出
     * @param data 数据源
     * @param head 表头
     * @param response response
     */
    public static void writeToBrowser(List<List<Object>> data, List<String> head, HttpServletResponse response) throws IOException {
        writeToBrowser(data,head,null,response);
    }

    /**
     * 通过浏览器导出
     * @param data 数据源
     * @param header 表头
     * @param sheet excel页面样式
     * @param response response
     */
    public static void writeToBrowser(List<List<Object>> data, List<String> header, Sheet sheet, HttpServletResponse response) throws IOException {
        OutputStream outputStream = getOutputStreamByBrowser(null,response);
        write(outputStream,data,header,sheet);
    }


    /**
     * 模板导出excel到指定文件
     * @param filePath 绝对路径, 如：/home/chenmingjian/Downloads/aaa.xlsx
     * @param data 数据源
     */
    public static void writeWithTemplateToFilePath(String filePath, List<? extends BaseRowModel> data) throws IOException {
        writeWithTemplateToFilePath(filePath,data,null);
    }

    /**
     * 模板导出excel到指定文件
     * @param filePath 绝对路径, 如：/home/chenmingjian/Downloads/aaa.xlsx
     * @param data 数据源
     * @param sheet excel页面样式
     */
    public static void writeWithTemplateToFilePath(String filePath, List<? extends BaseRowModel> data, Sheet sheet) throws IOException {
        writerByTemplate(getOutputStreamByFilePath(filePath),data,sheet);
    }

    /**
     * 带模板导出到浏览器
     * @param data 数据源
     * @param response response
     */
    public static void writeWithTemplateToBrowser(List<? extends BaseRowModel> data, HttpServletResponse response) throws IOException {
        writeWithTemplateToBrowser(null,data,response,null);
    }

    /**
     * 带模板导出到浏览器
     * @param fileName 文件名称 如：aaa
     * @param data 数据源
     * @param response response
     * @param sheet excel页面样式
     */
    public static void writeWithTemplateToBrowser(String fileName, List<? extends BaseRowModel> data, HttpServletResponse response, Sheet sheet) throws IOException {
        writerByTemplate(getOutputStreamByBrowser(fileName,response),data,sheet);
    }


    /**
     * 生成多Sheet的excel
     * @param filePath 绝对路径, 如：/home/chenmingjian/Downloads/aaa.xlsx
     */
    public static void writeWithMultipleSheet(String filePath, List<MultipleSheetProperty> multipleSheetProperties) throws IOException {
        if(CollectionUtils.isEmpty(multipleSheetProperties) || CollectionUtils.isEmpty(multipleSheetProperties.get(0).getData())){
            return;
        }

        OutputStream outputStream = new FileOutputStream(filePath);
        ExcelWriter writer = EasyExcelFactory.getWriter(outputStream);
        for (MultipleSheetProperty multipleSheetProperty : multipleSheetProperties) {
            Sheet sheet = multipleSheetProperty.getSheet() != null ? multipleSheetProperty.getSheet() : initSheet();
            sheet.setClazz(multipleSheetProperty.getData().get(0).getClass());
            writer.write(multipleSheetProperty.getData(), sheet);
        }

        writer.finish();
        outputStream.close();

    }


    /**
     * 通过浏览器获取到输出流
     * @param fileName 文件名称，如：aaa
     * @param response response
     */
    private static OutputStream getOutputStreamByBrowser(String fileName, HttpServletResponse response) throws IOException {
        //解决文件名中文不显示
        fileName = URLEncoder.encode(!StringUtils.isEmpty(fileName) ? fileName : "template", "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        return response.getOutputStream();
    }

    private static OutputStream getOutputStreamByFilePath(String filePath) throws FileNotFoundException {
        return new FileOutputStream(filePath);
    }

    public static void initHeader(Sheet sheet, List<String> header){
        if(header != null){
            List<List<String>> list = new ArrayList<>();
            header.forEach(h -> list.add(Collections.singletonList(h)));
            sheet.setHead(list);
        }
    }

    private static void writerByTemplate(OutputStream outputStream, List<? extends BaseRowModel> data, Sheet sheet) throws IOException {
        sheet = (sheet != null) ? sheet : initSheet();
        sheet.setClazz(data.get(0).getClass());
        ExcelWriter writer = EasyExcelFactory.getWriter(outputStream);
        writer.write(data,sheet);
        writer.finish();
        outputStream.close();
    }

    public static void write(OutputStream outputStream,List<List<Object>> data, List<String> header,Sheet sheet) throws IOException {
        sheet = (sheet != null) ? sheet : initSheet();
        initHeader(sheet,header);
        ExcelWriter writer = EasyExcelFactory.getWriter(outputStream);
        writer.write1(data,sheet);
        writer.finish();
        outputStream.close();
    }




    /*********************匿名内部类开始，可以提取出去******************************/

    @Data
    public static class MultipleSheetProperty {

        private List<? extends BaseRowModel> data;

        private Sheet sheet;
    }

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
    public static class ExcelListener<T> extends AnalysisEventListener<T> {

        private List<T> data = new ArrayList<>();

        /**
         * 逐行解析
         * object : 当前行的数据
         */
        @Override
        public void invoke(T object, AnalysisContext context) {
            //当前行
            // context.getCurrentRowNum()
            if (object != null) {
                data.add(object);
            }
        }


        /**
         * 解析完所有数据后会调用该方法
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            //解析结束销毁不用的资源
        }

    }

    /************************匿名内部类结束，可以提取出去***************************/

}
