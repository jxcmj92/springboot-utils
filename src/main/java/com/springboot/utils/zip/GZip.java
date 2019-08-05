package com.springboot.utils.zip;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by bruce on 3/6/15.
 */
public class GZip {
    public static final int BUFFER = 8096;

    public static byte[] gzip(String source) {
        try {
            return gzip(source.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] gzip(byte[] source) {
        if (source == null || source.length == 0) {
            return null;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(source);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            gzip(bais, baos);
            return baos.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static byte[] gunzip(byte[] source) {
        if (source == null || source.length == 0) {
            return null;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(source);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            gunzip(bais, baos);
            return baos.toByteArray();
        }
        catch (Exception e) {
            return null;
        }

    }

    /**
     * 数据压缩
     *
     * @param is
     * @param os
     * @throws Exception
     */
    public static void gzip(InputStream is, OutputStream os) throws Exception {

        GZIPOutputStream gos = new GZIPOutputStream(os);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            gos.write(data, 0, count);
        }


        gos.finish();

        gos.flush();
        gos.close();
    }

    /**
     * 数据解压缩
     *
     * @param is
     * @param os
     * @throws Exception
     */
    public static void gunzip(InputStream is, OutputStream os) throws Exception {

        GZIPInputStream gis = new GZIPInputStream(is);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = gis.read(data, 0, BUFFER)) != -1) {
            os.write(data, 0, count);
        }
        gis.close();
    }


    public static void main(String[] args) throws IOException {
        InputStream fileStream = new FileInputStream(new File("/home/chenmingjian/Downloads/数据模型列表.xls"));
        byte[] bytes = IOUtils.toByteArray(fileStream);
        byte[] gzip = gzip(bytes);
    }

}
