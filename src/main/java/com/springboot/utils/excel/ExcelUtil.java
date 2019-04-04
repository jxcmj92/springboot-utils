package com.springboot.utils.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.springboot.utils.excel.test.TableHeaderExcelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-3-18 16:16
 */
@Slf4j
public class ExcelUtil {

   private static Sheet initSheet;

   static {
      initSheet = new Sheet(1, 0);
      initSheet.setSheetName("sheet");
      //设置自适应宽度
      initSheet.setAutoWidth(Boolean.TRUE);
   }

   public static List<Object> readLessThan1000Row(String filePath){
      return readLessThan1000Row(filePath,null);
   }

   /**
    * 读07版小于1000行数据
    * filePath 文件绝对路径
    * initSheet ：
    *      sheetNo: sheet页码，默认为1
    *      headLineMun: 从第几行开始读取数据，默认为0, 表示从第一行开始读取
    *      clazz: 返回数据List<Object> 中Object的类名
    */
   public static List<Object> readLessThan1000Row(String filePath, Sheet sheet){
      if(!StringUtils.hasText(filePath)){
         return null;
      }

      sheet = sheet != null ? sheet : initSheet;

      InputStream fileStream = null;
      try {
         fileStream = new FileInputStream(filePath);
         return EasyExcelFactory.read(fileStream, sheet);
      } catch (FileNotFoundException e) {
         log.info("找不到文件或文件路径错误, 文件：{}", filePath);
      }finally {
         try {
            if(fileStream != null){
               fileStream.close();
            }
         } catch (IOException e) {
            log.info("excel文件读取失败, 失败原因：{}", e);
         }
      }
      return null;
   }

   public static List<Object> readMoreThan1000Row(String filePath, AnalysisEventListener listener){
      return readMoreThan1000Row(filePath,listener, null);
   }

   public static List<Object> readMoreThan1000Row(String filePath, AnalysisEventListener listener,Sheet sheet){
      if(!StringUtils.hasText(filePath)){
         return null;
      }

      sheet = sheet != null ? sheet : initSheet;

      InputStream fileStream = null;
      try {
         fileStream = new FileInputStream(filePath);
         ExcelListener excelListener = new ExcelListener();
         EasyExcelFactory.readBySax(fileStream, sheet, excelListener);
         return excelListener.getDatas();
      } catch (FileNotFoundException e) {
         log.error("找不到文件或文件路径错误, 文件：{}", filePath);
      }finally {
         try {
            if(fileStream != null){
               fileStream.close();
            }
         } catch (IOException e) {
            log.error("excel文件读取失败, 失败原因：{}", e);
         }
      }
      return null;
   }

   public static void writeWithTemplateAndStyle(String filePath, List<? extends BaseRowModel> data, Sheet sheet){
      if(CollectionUtils.isEmpty(data)){
         return;
      }

      sheet = (sheet != null) ? sheet : initSheet;
      sheet.setClazz(data.get(0).getClass());

      OutputStream outputStream = null;
      ExcelWriter writer = null;
      try {
         outputStream = new FileOutputStream(filePath);
         writer = EasyExcelFactory.getWriter(outputStream);
         writer.write(data,sheet);
      } catch (FileNotFoundException e) {
         log.error("找不到文件或文件路径错误, 文件：{}", filePath);
      }finally {
         try {
            if(writer != null){
               writer.finish();
            }

            if(outputStream != null){
               outputStream.close();
            }
         } catch (IOException e) {
            log.error("excel文件读取失败, 失败原因：{}", e);
         }
      }

   }


   public static void writeWithTemplate(String filePath, List<? extends BaseRowModel> data){
         writeWithTemplateAndStyle(filePath,data,null);

   }

   /**
    *
    * @param filePath 绝对路径, 如：/home/chenmingjian/Downloads/aaa.xlsx
    * @param data 数据源
    * @param sheet excle页面样式
    * @param head 表头
    */
   public static void writeSimpleByStle(String filePath, List<List<Object>> data, List<String> head, Sheet sheet){
      sheet = (sheet != null) ? sheet : initSheet;

      if(head != null){
         List<List<String>> list = new ArrayList<>();
         head.forEach(h -> list.add(Collections.singletonList(h)));
         sheet.setHead(list);
      }

      OutputStream outputStream = null;
      ExcelWriter writer = null;
      try {
         outputStream = new FileOutputStream(filePath);
         writer = EasyExcelFactory.getWriter(outputStream);
         writer.write1(data,sheet);
      } catch (FileNotFoundException e) {
         log.error("找不到文件或文件路径错误, 文件：{}", filePath);
      }finally {
         try {
            if(writer != null){
               writer.finish();
            }

            if(outputStream != null){
               outputStream.close();
            }

         } catch (IOException e) {
            log.error("excel文件读取失败, 失败原因：{}", e);
         }
      }

   }


   /**
    * 生成excle
    * @param filePath  绝对路径, 如：/home/chenmingjian/Downloads/aaa.xlsx
    * @param data 数据源
    * @param head 表头
    */
   public static void writeBySimple(String filePath, List<List<Object>> data, List<String> head){
      writeSimpleByStle(filePath,data,head,null);
   }

   public static void main(String[] args) {
//      String filePath = "/home/chenmingjian/Downloads/学生表.xlsx";
//      Sheet initSheet = new Sheet(1,0);
//      List<Object> objects = readMoreThan1000Row(filePath);
//      objects.forEach(System.out::println);

//      Sheet sheet1 = new Sheet(1, 0);
//      sheet1.setSheetName("学生");
//      List<List<Object>> list = new ArrayList<>();
//      list.add(Arrays.asList("111","222","333"));
//      list.add(Arrays.asList("111","222","333"));
//      list.add(Arrays.asList("111","222","333"));
//
//      List<String> head = Arrays.asList("111", "333", "222");
//
//      ExcelUtil.writeBySimple("/home/chenmingjian/Downloads/aaa.xlsx",list,head);

//      ExcelUtil.write("/home/chenmingjian/Downloads/aaa.xlsx",list);




//      ArrayList<TableHeaderExcelProperty> list = new ArrayList<>();
//      for(int i = 0; i < 4; i++){
//         TableHeaderExcelProperty tableHeaderExcelProperty = new TableHeaderExcelProperty();
//         tableHeaderExcelProperty.setName("cmj" + i);
//         tableHeaderExcelProperty.setAge(22 + i);
//         tableHeaderExcelProperty.setSchool("清华大学" + i);
//         list.add(tableHeaderExcelProperty);
//      }
//
//      ExcelUtil.writeWithTemplate("/home/chenmingjian/Downloads/aaa.xlsx",list);

      ArrayList<MultipleSheelPropety> list1 = new ArrayList<>();

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

         MultipleSheelPropety multipleSheelPropety = new MultipleSheelPropety();
         multipleSheelPropety.setData(list);
         multipleSheelPropety.setSheet(sheet);

         list1.add(multipleSheelPropety);

      }


      ExcelUtil.writeWithMultipleSheel("/home/chenmingjian/Downloads/aaa.xlsx",list1);



   }

   public static void writeWithMultipleSheel(String filePath,List<MultipleSheelPropety> multipleSheelPropetys){
      if(CollectionUtils.isEmpty(multipleSheelPropetys) || CollectionUtils.isEmpty(multipleSheelPropetys.get(0).getData())){
         return;
      }

      OutputStream outputStream = null;
      ExcelWriter writer = null;
      try {
         outputStream = new FileOutputStream(filePath);
         writer = EasyExcelFactory.getWriter(outputStream);
         for (MultipleSheelPropety multipleSheelPropety : multipleSheelPropetys) {
            Sheet sheet = multipleSheelPropety.getSheet() != null ? multipleSheelPropety.getSheet() : initSheet;
            sheet.setClazz(multipleSheelPropety.getData().get(0).getClass());
            writer.write(multipleSheelPropety.getData(), sheet);
         }

      } catch (FileNotFoundException e) {
         log.error("找不到文件或文件路径错误, 文件：{}", filePath);
      }finally {
         try {
            if(writer != null){
               writer.finish();
            }

            if(outputStream != null){
               outputStream.close();
            }
         } catch (IOException e) {
            log.error("excel文件读取失败, 失败原因：{}", e);
         }
      }

   }

   @Data
   public static class MultipleSheelPropety{

      private List<? extends BaseRowModel> data;

      private Sheet sheet;
   }


}
