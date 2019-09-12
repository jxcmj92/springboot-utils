package com.springboot.utils.springboot_test;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.springboot.utils.excel.EasyExcelTest;
import com.springboot.utils.excel.EasyExcelUtil;
import com.springboot.utils.zip.ZipFileUtil2;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.springboot.utils.zip.GZip.gzip;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-7-1 11:20
 */
@RestController
public class Test {

    @GetMapping("/download")
    public void test4(/*HttpServletResponse response*/) throws IOException {
        String filePath = "/home/chenmingjian/Downloads/新建+XLSX+工作表.xlsx";
        List<List<String>> objects = EasyExcelUtil.readMoreThan1000Row(filePath);
////        ExcelUtil.writeToBrowser(objects,null,response);
//
//        List<SaleExcel> saleExcels = new ArrayList<>();
//        SaleExcel saleExcel = new SaleExcel();
//        saleExcels.add(saleExcel);
//        ExcelUtil.writeWithTemplateToBrowser(saleExcels,response);

        List<List<String>> lists = get();

        ArrayList<EasyExcelTest.TableHeaderExcelProperty> data = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            EasyExcelTest.TableHeaderExcelProperty tableHeaderExcelProperty = new EasyExcelTest.TableHeaderExcelProperty();
            tableHeaderExcelProperty.setName("cmj" + i);
            tableHeaderExcelProperty.setAge(22 + i);
            tableHeaderExcelProperty.setSchool("清华大学" + i);
            data.add(tableHeaderExcelProperty);
        }
        EasyExcelUtil.writeWithTemplateToFilePath( "/home/chenmingjian/Downloads/测试.xlsx",data);
    }


    public List<List<String>> get() throws IOException {
        String filePath = "/home/chenmingjian/Downloads/新建+XLSX+工作表.xlsx";
        BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(filePath));
        EasyExcelUtil.ExcelListener excelListener = new EasyExcelUtil.ExcelListener();
        EasyExcelFactory.readBySax(fileStream, new Sheet(1, 0), excelListener);
        List data = excelListener.getData();
        fileStream.close();
        return data;
    }

    @GetMapping("/zip")
    public void test3(HttpServletResponse response) throws IOException {
        ArrayList<InputStream> inputStreams = new ArrayList<>();
        InputStream fileStream = new FileInputStream(new File("/home/chenmingjian/Downloads/数据模型列表.xls"));
        InputStream fileStream2 = new FileInputStream(new File("/home/chenmingjian/Downloads/template.xls"));
        inputStreams.add(fileStream);
        inputStreams.add(fileStream2);

        byte[] bytes = ZipFileUtil2.compressDir(inputStreams);
        IOUtils.write(bytes, response.getOutputStream());
        response.flushBuffer();

    }


    /**
     * 文件流压缩
     * @author momo
     * @since 2018-8-8
     * @param baisByte 需要压缩的字节输出流(ByteArrayOutputStream)的字节数组
     * @param fileName 需要压缩的文件名
     * @return  压缩后字节数组输出流转为的字符串
     * @throws IOException
     */
    public static String zipByteArrayOutputStream(byte[] baisByte,String fileName) throws IOException {

        //1.将需要压缩的字节输出流，转为字节数组输入流，
        ByteArrayInputStream bais = new ByteArrayInputStream(baisByte);

        //2.创建字节数组输出流，用于返回压缩后的输出流字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //3.创建压缩输出流
        ZipOutputStream zipOut = new ZipOutputStream(baos);

        //zipOut.setEncoding("GBK");//设置编码格式，否则中文文件名乱码

        //zipOut.setMethod(ZipOutputStream.DEFLATED);//进行压缩存储

        //zipOut.setLevel(Deflater.BEST_COMPRESSION);//压缩级别值为0-9共10个级别(值越大，表示压缩越利害)

        //4.设置ZipEntry对象，并对需要压缩的文件命名
        zipOut.putNextEntry(new ZipEntry(fileName)) ;


        //5.读取要压缩的字节输出流，进行压缩
        int temp = 0 ;
        while((temp=bais.read())!=-1){
            zipOut.write(temp) ;    // 压缩输出
        }

        // 关闭流
        bais.close();
        zipOut.close();

        //字节数组输出流转为字符串并设置编码
        String result =new String(baos.toByteArray(), "ISO-8859-1");

        baos.close();// 关闭流

        return result;
    }






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
        ArrayList<EasyExcelTest.TableHeaderExcelProperty> data = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            EasyExcelTest.TableHeaderExcelProperty tableHeaderExcelProperty = new EasyExcelTest.TableHeaderExcelProperty();
            tableHeaderExcelProperty.setName("cmj" + i);
            tableHeaderExcelProperty.setAge(22 + i);
            tableHeaderExcelProperty.setSchool("清华大学88888888888888" + i);
            data.add(tableHeaderExcelProperty);
        }

        EasyExcelUtil.writeWithTemplateToBrowser(data,response);

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
    public JSONObject test(@RequestBody Object object){
        System.out.println(JSONObject.toJSONString(object));
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
