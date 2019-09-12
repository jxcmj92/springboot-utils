package com.springboot.utils.excel;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

public class PoiExcelUtil {

    private static final String XLSX = "xlsx";
    private static final String XLS = "xls";


    /**
     * @param filePath 决定路径
     */
    public static List<List<String>> readExcel(String filePath) {
        String fileSuffix = filePath.substring(filePath.lastIndexOf('.') + 1);

        if (!XLSX.equalsIgnoreCase(fileSuffix) && !XLS.equalsIgnoreCase(fileSuffix)) {
            throw new RuntimeException("不支持的格式，请上传xls或xlsx格式的文件");
        }

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            return readExcel(fileSuffix, fileInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    private static String getCellStringValue(Cell cell) {
        Object obj = getCellValue(cell);
        String value = null;

        if (null != obj) {
            value = String.valueOf(obj);
        }

        return value;
    }

    private static Object getCellValue(Cell cell) {
        Object value = null;
        if (null != cell) {
            switch (cell.getCellTypeEnum()) {
                case BLANK:
                    break;
                case ERROR:
                    value = cell.getErrorCellValue();
                    break;
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case BOOLEAN:
                    value = cell.getBooleanCellValue();
                    break;
                case FORMULA:
                    value = cell.getDateCellValue().getTime();
                    break;
                case NUMERIC:
                    value = cell.getNumericCellValue();
                    DecimalFormat df = new DecimalFormat("0");
                    value = df.format(value);
                    break;
                case _NONE:
            }
        }

        return value;
    }


    /**
     * @param fileSuffix 后缀名称：xlsx 或 xls
     */
    private static List<List<String>> readExcel(String fileSuffix, InputStream inputStream) {

        Workbook workbook = null;
        POIFSFileSystem fileSystem = null;
        List<List<String>> values = null;

        try {

            if (XLSX.equalsIgnoreCase(fileSuffix)) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (XLS.equalsIgnoreCase(fileSuffix)) {
                fileSystem = new POIFSFileSystem(inputStream);
                workbook = new HSSFWorkbook(fileSystem);
            }

            if (null == workbook) {
                throw new RuntimeException("can not read workbook");
            }

            values = getLists(workbook);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(fileSystem);
        }

        return values;
    }




    private static List<List<String>> getLists(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        int lastRow = sheet.getLastRowNum();
        List<List<String>> values = new ArrayList<>();


        for (int i = 0; i <= lastRow; i++) {
            Row row = sheet.getRow(i);
            if (null == row) {
                continue;
            }
            short cellNum = row.getLastCellNum();
            List<String> rowValue = new ArrayList<>();

            for (int j = 0; j <= cellNum; j++) {
                Cell cell = row.getCell(j);
                if (null != cell) {
                    String value = getCellStringValue(cell);
                    rowValue.add(value);
                } else {
                    rowValue.add("");
                }
            }

            values.add(rowValue);
        }


        return values;
    }

    /**
     * 过滤空数据
     */
    private static void filterList(List<List<String>> values) {
        // 过滤空数据
        // 以第一行为准，第一行有多少列
        if (!CollectionUtils.isEmpty(values)) {

            List<String> title = values.get(0);
            int last = title.size() - 1;
            int toDelete = 0;

            // 如果第一行最后一列为空
            for (int i = last; i >= 0; i--) {
                if (StringUtils.isEmpty(title.get(i))) {
                    toDelete++;
                } else {
                    break;
                }
            }

            // 确定列的数量
            int colNum = title.size() - toDelete;
            int len = values.size();

            for (int i = 0; i < len; i++) {

                List<String> row = values.get(i);
                List<String> newRow;

                if (row.size() > colNum) {
                    newRow = new ArrayList(row.subList(0, colNum));
                } else {
                    newRow = new ArrayList<>(row);
                }

                if (newRow.size() < colNum) {
                    int jlen = colNum - newRow.size();
                    for (int j = 0; j < jlen; j++) {
                        newRow.add("");
                    }
                }

                values.set(i, newRow);
            }


            // 过滤掉空行
            Iterator<List<String>> it = values.iterator();
            while (it.hasNext()) {
                List<String> row = it.next();
                boolean allEmpty = row.stream().allMatch(item -> StringUtils.isEmpty(item));
                if (allEmpty) {
                    it.remove();
                }
            }
        }
    }

}
