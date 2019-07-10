package com.springboot.utils.ireport;

import com.springboot.utils.ireport.entry.Model2Entry;
import com.springboot.utils.ireport.entry.ModelEntry;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-4-19 15:43
 */
@RestController
public class Test2 {

    @Autowired
    private ApplicationContext appContext;

    public List<ModelEntry> getProductList() {
        List<ModelEntry> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            ModelEntry product = new ModelEntry();
            product.setName("名称" + i);
            list.add(product);
        }

        return list;
    }

    public List<Model2Entry> getProductLists() {
        List<Model2Entry> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            ModelEntry product = new ModelEntry();
            Model2Entry model2Entry = new Model2Entry();
            product.setName("名称" + i);
            model2Entry.setModelEntries(product);
            list.add(model2Entry);
        }

        return list;
    }


    @RequestMapping(path = "/pdfExport", method = RequestMethod.GET)
    public String pdfExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JRDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(getProductLists());
        Map params = new HashMap();
        params.put("Parameter1", "hello 你好");

        //编译jrxml文件，生成jasper文件
        URL url = this.getClass().getClassLoader().getResource("jasperreport/test.jrxml");
        JasperCompileManager.compileReportToFile(url.getPath());
        File jasperFile = ResourceUtils.getFile("classpath:jasperreport/test.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrBeanCollectionDataSource);
        JRPdfExporter pdf = new JRPdfExporter();
        pdf.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        pdf.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
        String fileName = new String("测试.pdf".getBytes("utf-8"), "ISO_8859_1");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        pdf.exportReport();
        return null;
    }




}
