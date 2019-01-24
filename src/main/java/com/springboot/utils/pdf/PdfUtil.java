package com.springboot.utils.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: pdf工具类
 * @author: chenmingjian
 * @date: 19-1-2 10:57
 */
public class PdfUtil {

    /**
     * 在原有的pdf模板上生成一份新的pdf文档
     * @param originalPdfFilePath  原始的pdf模板绝对路径
     * @param newPdfFilePath  生成新的pdf文档的绝对路径
     * @param map  新的pdf文档中要添加的字段
     * @return:
     */
    public static void exportPdf(String originalPdfFilePath, String newPdfFilePath, Map<String,Object> map) throws IOException, DocumentException {
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        out = new FileOutputStream(newPdfFilePath);
        reader = new PdfReader(originalPdfFilePath);
        bos = new ByteArrayOutputStream();
        stamper = new PdfStamper(reader, bos);
        AcroFields form = stamper.getAcroFields();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            form.setField(entry.getKey(), String.valueOf(entry.getValue()));
        }
        // 如果为false那么生成的PDF文件还能编辑，一定要设为true
        stamper.setFormFlattening(true);
        stamper.close();
        Document doc = new Document();
        PdfCopy copy = new PdfCopy(doc, out);
        doc.open();


        PdfImportedPage importPage;
        ///循环是处理成品只显示一页的问题
        for (int i=1;i<=reader.getNumberOfPages();i++) {
            importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i);
            copy.addPage(importPage);
        }
        doc.close();
    }


    public static void main(String[] args) throws IOException, DocumentException {
        // 模板路径
        String  originalPdfFilePath = "/home/chenmingjian/Downloads/赊账式销售合同.pdf";
        // 生成的新文件路径
        String newPdfFilePath = "/home/chenmingjian/Downloads/赊账式销售合同完整版.pdf";

        Map<String, Object> map = new HashMap<>(16);
        map.put("sellerName","陈明建");
        map.put("sellerAddress","深圳市福田区新沙路北侧国都高尔夫花园绿陶轩17B");
        map.put("sellerIdCard","360424199587546215");
        map.put("sellerPhoneNo","18375698456");
        map.put("sellerEmail","1757111358@ qq.com");
        map.put("sellerFaxNo","360424199587546215");

        map.put("buyerName","陈明建");
        map.put("buyerAddress","江西省修水县");
        map.put("buyerIdCard","360424199587546215");
        map.put("buyerPhoneNo","18375698456");
        map.put("buyerEmail","1757111358@ qq.com");
        map.put("buyerFaxNo","360424199587546215");

        map.put("operatorName","陈明建");
        map.put("operatorAddress","江西省修水县江西省修水县江西省修水县江西省修水县江西省修水县");
        map.put("operatorIdCard","360424199587546215");
        map.put("operatorPhoneNo","18375698456");
        map.put("operatorEmail","1757111358@ qq.com");
        map.put("operatorFaxNo","360424199587546215");

        map.put("amount","100000");
        map.put("payStyle","微信支付");
        map.put("periods","6");
        map.put("stageHandlingFee","600");
        map.put("onePeriodAmount","100");
        exportPdf(originalPdfFilePath,newPdfFilePath,map);
    }
}
