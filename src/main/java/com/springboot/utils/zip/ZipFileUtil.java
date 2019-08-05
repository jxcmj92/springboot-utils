package com.springboot.utils.zip;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @description: 压缩/解压
 * @author: chenmingjian
 * @date: 19-7-24 15:52
 */
public class ZipFileUtil {

    /**
     * @param args 主方法
     */
    public static void main(String[] args) throws IOException {

        String targetPath = "/home/chenmingjian/Downloads/zip/oo.zip";
        String sourcePath1 = "/home/chenmingjian/Downloads/数据模型列表.xls";
        String sourcePath2 = "/home/chenmingjian/Downloads/参数模板.xlsx";

        // TODO Auto-generated method stub
        //第一个参数是需要压缩的源路径；第二个参数是压缩文件的目的路径，这边需要将压缩的文件名字加上去
//        compress("/home/chenmingjian/Downloads/freemarkhtml","/home/chenmingjian/Downloads/oo.zip");
        InputStream fileStream = new FileInputStream(new File(sourcePath1));
        InputStream fileStream2 = new FileInputStream(new File(sourcePath2));
        ArrayList<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(fileStream);
        inputStreams.add(fileStream2);
//        compressDir(inputStreams,targetPath);
//        compress(fileStream,targetPath);

//        byte[] bytes = IOUtils.toByteArray("/home/chenmingjian/Downloads/数据模型列表.xls");
//        FileInputStream fileInputStream = byteToFileInputStream(bytes);


        File fileDir = new File("/home/chenmingjian/Downloads/zip/");
        compress("/home/chenmingjian/Downloads/zip/",targetPath);

    }

    public static void compressDir(List<InputStream> inputStreams, String targetPath){
        inputStreams.forEach(inputStream -> compress(inputStream,targetPath));
    }

    public static void compress(InputStream inputStream, String targetPath){
        if(StringUtils.isBlank(targetPath)){
            throw new RuntimeException(targetPath + "不存在");
        }

        File zipFile = new File(targetPath);

        try {

            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ZipEntry entry = new ZipEntry(UUID.randomUUID().toString() + ".xls");
            zos.putNextEntry(entry);            int count;
            byte[] buf = new byte[1024];
            while ((count = bis.read(buf)) != -1) {
                zos.write(buf, 0, count);
            }
            bis.close();
            zos.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }



    }




    /**s
     * 压缩文件
     * @param srcFilePath 压缩源路径
     * @param destFilePath 压缩目的路径
     */
    public static void compress(String srcFilePath, String destFilePath) {
        //
        File src = new File(srcFilePath);

        if (!src.exists()) {
            throw new RuntimeException(srcFilePath + "不存在");
        }
        File zipFile = new File(destFilePath);

        try {

            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            String baseDir = "";
            compressbyType(src, zos, baseDir);
            zos.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
    /**
     * 按照原路径的类型就行压缩。文件路径直接把文件压缩，
     * @param src
     * @param zos
     * @param baseDir
     */
    private static void compressbyType(File src, ZipOutputStream zos,String baseDir) {

        if (!src.exists()) {
            return;
        }
        System.out.println("压缩路径" + baseDir + src.getName());
        //判断文件是否是文件，如果是文件调用compressFile方法,如果是路径，则调用compressDir方法；
        if (src.isFile()) {
            //src是文件，调用此方法
            compressFile(src, zos, baseDir);

        } else if (src.isDirectory()) {
            //src是文件夹，调用此方法
            compressDir(src, zos, baseDir);

        }

    }

    private static void compressFile(byte[] bytes, ZipOutputStream zos,String baseDir) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(byteToFileInputStream(bytes));
        ZipEntry entry = new ZipEntry("123");
        zos.putNextEntry(entry);
        int count;
        byte[] buf = new byte[1024];
        while ((count = bis.read(buf)) != -1) {
            zos.write(buf, 0, count);
        }
        bis.close();
    }

    /**
     * 压缩文件
     */
    private static void compressFile(File file, ZipOutputStream zos,String baseDir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zos.putNextEntry(entry);
            int count;
            byte[] buf = new byte[1024];
            while ((count = bis.read(buf)) != -1) {
                zos.write(buf, 0, count);
            }
            bis.close();

        } catch (Exception e) {
            // TODO: handle exception

        }
    }

    /**
     * 压缩文件夹
     */
    private static void compressDir(File dir, ZipOutputStream zos,String baseDir) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if(files.length == 0){
            try {
                zos.putNextEntry(new ZipEntry(baseDir + dir.getName()+File.separator));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (File file : files) {
            compressbyType(file, zos, baseDir + dir.getName() + File.separator);
        }
    }


    /**
     * 字节转FileInputStream
     *
     * @param bytes
     * @return
     */
    public static FileInputStream byteToFileInputStream(byte[] bytes) throws IOException {
        return new FileInputStream(Arrays.toString(bytes));
    }





}
