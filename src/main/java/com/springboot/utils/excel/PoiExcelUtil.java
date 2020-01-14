package com.springboot.utils.excel;

import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class PoiExcelUtil {

    private static final String XLSX = "xlsx";
    private static final String XLS = "xls";

    public static class ReadUtil{
        /**
         * @param filePath 绝对路径, 如：/home/chenmingjian/Downloads/aaa.xlsx
         */
        public static PoiExcelResult readExcel(String filePath) {
            PoiExcelResult poiExcelResult = new PoiExcelResult();
            String fileSuffix = filePath.substring(filePath.lastIndexOf('.') + 1);
            if (!XLSX.equalsIgnoreCase(fileSuffix) && !XLS.equalsIgnoreCase(fileSuffix)) {
                poiExcelResult.setSuccess(false);
                poiExcelResult.setErrorMessage("不支持的格式，请上传xls或xlsx格式的文件");
            }

            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                return readExcel(fileSuffix, fileInputStream);
            } catch (Exception e) {
                poiExcelResult.setSuccess(false);
                poiExcelResult.setErrorMessage("未知服务器错误");
            }


            return poiExcelResult;
        }



        private static String getCellValue(Cell cell) {
            if(cell == null){
                return "";
            }

            Object value = null;
            switch (cell.getCellType()) {
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

            return String.valueOf(value);
        }


        /**
         * @param fileSuffix 后缀名称：xlsx 或 xls
         */
        private static PoiExcelResult readExcel(String fileSuffix, InputStream inputStream) {
            PoiExcelResult poiExcelResult = new PoiExcelResult();
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
                    poiExcelResult.setSuccess(false);
                    poiExcelResult.setErrorMessage("excel读取类型错误");
                    return poiExcelResult;
                }

                values = getLists(workbook);

            } catch (IOException e) {
                poiExcelResult.setErrorMessage("未知服务器错误");
            } finally {
                IOUtils.closeQuietly(workbook);
                IOUtils.closeQuietly(fileSystem);
            }

            poiExcelResult.setData(values);
            return poiExcelResult;
        }

        /**
         * 获取数据
         */
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
                    if (null == cell) {
                        rowValue.add("");
                        continue;
                    }

                    boolean isMerge = isMergedRegion(sheet, i, cell.getColumnIndex());
                    // 判断是否具有合并单元格
                    if (isMerge) {
                        String rs = getMergedRegionValue(sheet, row.getRowNum(), cell.getColumnIndex());
                        rowValue.add(rs);
                    } else {
                        rowValue.add(getCellValue(cell));
                    }
                }

                values.add(rowValue);
            }


            return values;
        }


        /**
         * 判断指定的单元格是否是合并单元格
         * @param rowNum 行号
         * @param column 列号
         */
        private static boolean isMergedRegion(Sheet sheet, int rowNum, int column) {
            int sheetMergeCount = sheet.getNumMergedRegions();
            for (int i = 0; i < sheetMergeCount; i++) {
                CellRangeAddress range = sheet.getMergedRegion(i);
                int firstColumn = range.getFirstColumn();
                int lastColumn = range.getLastColumn();
                int firstRow = range.getFirstRow();
                int lastRow = range.getLastRow();
                if (rowNum >= firstRow && rowNum <= lastRow) {
                    if (column >= firstColumn && column <= lastColumn) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * 获取合并单元格的值
         * @param rowNum 行号
         * @param column 列号
         */
        private static String getMergedRegionValue(Sheet sheet, int rowNum, int column) {
            int sheetMergeCount = sheet.getNumMergedRegions();

            for (int i = 0; i < sheetMergeCount; i++) {
                CellRangeAddress ca = sheet.getMergedRegion(i);
                int firstColumn = ca.getFirstColumn();
                int lastColumn = ca.getLastColumn();
                int firstRow = ca.getFirstRow();
                int lastRow = ca.getLastRow();

                if (rowNum >= firstRow && rowNum <= lastRow) {
                    if (column >= firstColumn && column <= lastColumn) {
                        Row fRow = sheet.getRow(firstRow);
                        Cell fCell = fRow.getCell(firstColumn);
                        return getCellValue(fCell);
                    }
                }
            }

            return null;
        }

        @Data
        public static class PoiExcelResult{

            /**
             * 是否成功
             */
            private boolean success = true;

            /**
             * 失败原因
             */
            private String errorMessage;

            /**
             * 数据
             */
            private List<List<String>> data;

        }
    }

    /**
     * 写入到Excel工具类
     */
    public static class WriteUtil{

        /**
         * 合并单元格
         * @param sheet sheet页
         * @param value 值
         * @param firstRow 起始行
         * @param lastRow 终止行
         * @param firstCol 起始列
         * @param lastCol 终止列
         */
        public static void addMergedRegion(Sheet sheet, Object value,int firstRow, int lastRow, int firstCol, int lastCol) {
            if (lastRow > firstRow || lastCol > firstCol) {
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
                Row row = sheet.getRow(firstRow) != null ? sheet.getRow(firstRow) : sheet.createRow(firstRow);
                Cell cell = row.getCell(firstCol) != null ? row.getCell(firstCol) : row.createCell(firstCol);
                cell.setCellValue(String.valueOf(value));
            }
        }


        /**
         * 为行设置背景色
         * @param workbook 工作溥
         * @param rowNum   行号
         * @param index    颜色枚举 如：IndexedColors.TAN.index
         */
        public static void addRowGroundColor(Workbook workbook, int rowNum, short index) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowNum) == null ? sheet.createRow(rowNum) : sheet.getRow(rowNum);
            CellStyle cellStyle = null;
            for (Cell cell : row) {
                cellStyle = (row.getRowStyle() == null || cell.getCellStyle() == null) ? workbook.createCellStyle() : cell.getCellStyle();
                cellStyle.setFillForegroundColor(index);
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                setAllBorder(cellStyle);
                cell.setCellStyle(cellStyle);
            }

            short lastCellNum = row.getLastCellNum();
            if(lastCellNum == -1){
                row.setRowStyle(cellStyle);
            }

        }

        /**
         * 设置所有行的高度
         * @param sheet sheet 页
         * @param height 高度值
         */
        public static void addAllRowHeight(Sheet sheet,int height){
            sheet.forEach(row -> row.setHeight((short) height));
        }

        /**
         * 设置所有列的宽度
         * @param sheet sheet 页
         * @param width 宽度值
         */
        public static void addAllColumnWidth(Sheet sheet,int width){
            sheet.setDefaultColumnWidth(width);
        }

        /**
         * 设置默认单元格的高度和宽度
         * @param sheet sheet 页
         * @param rowHeight 单元格高度值
         * @param columnWidth 单元格宽度值
         */
        public static void addDefaultHeightAndWidth(Sheet sheet, int rowHeight, int columnWidth){
            addAllRowHeight(sheet,rowHeight);
            addAllColumnWidth(sheet,columnWidth);
        }

        /**
         * 设置对齐样式，如上下居中，左右居中等
         * @param workbook 工作溥
         * @param rowNum 行号
         * @param horizontalType 水平对齐类型，如左右居中
         * @param verticalAlignmentType 垂直对齐类型，如垂直居中
         */
        public static void addAlignment(Workbook workbook,int rowNum, HorizontalAlignment horizontalType, VerticalAlignment verticalAlignmentType) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowNum) == null ? sheet.createRow(rowNum) : sheet.getRow(rowNum);
            row.forEach(cell -> {
                CellStyle cellStyle = (row.getRowStyle() == null || cell.getCellStyle() == null) ? workbook.createCellStyle() : cell.getCellStyle();
                cellStyle.setAlignment(horizontalType);
                cellStyle.setVerticalAlignment(verticalAlignmentType);
                cell.setCellStyle(cellStyle);
            });

        }

        /**
         * 设置对齐样式，如上下居中，左右居中等
         * @param workbook 工作溥
         * @param horizontalType 水平对齐类型，如左右居中
         * @param verticalAlignmentType 垂直对齐类型，如垂直居中
         */
        public static void addAllAlignment(Workbook workbook, HorizontalAlignment horizontalType, VerticalAlignment verticalAlignmentType) {
            Sheet sheet = workbook.getSheetAt(0);
            sheet.forEach(row -> addAlignment(workbook,row.getRowNum(),horizontalType,verticalAlignmentType));
        }


        /**
         * 设置某行字体
         * @param workbook 工作溥
         * @param rowNum 行号
         * @param font 字体
         */
        public static void addRowFont(Workbook workbook,int rowNum,Font font){
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowNum) == null ? sheet.createRow(rowNum) : sheet.getRow(rowNum);
            row.forEach(column ->{
                CellStyle cellStyle = column.getCellStyle() == null ? workbook.createCellStyle() : column.getCellStyle();
                cellStyle.setFont(font);
            });
        }


        /*
         * 设置单元格边框样式
         * BorderStyle.BORDER_DOUBLE 双边线
         * BorderStyle.BORDER_THIN 细边线
         * BorderStyle.BORDER_MEDIUM 中等边线
         * BorderStyle.BORDER_DASHED 虚线边线
         * BorderStyle.BORDER_HAIR 小圆点虚线边线
         * BorderStyle.BORDER_THICK 粗边线
         */
        private static void setAllBorder(CellStyle cellStyle){
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);//下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
            cellStyle.setBorderRight(BorderStyle.THIN);//右边框
            cellStyle.setBorderTop(BorderStyle.THIN);//上边框
//             cellStyle.setBottomBorderColor(IndexedColors.GREEN.getIndex()); //设置边框颜色
        }


        /**
         * 插入图片
         * @param base64string 图片base64
         * @param startColumn 开始列
         * @param endColumn  结束列
         * @param startRowNum 开始行
         * @param endRowNum 结束行
         */
        public static void addImg(Workbook workbook,String base64string,int startColumn, int endColumn,int startRowNum, int endRowNum) throws IOException {
            if(StringUtils.isEmpty(base64string)){
                return;
            }

            List<String> imgTypes = Arrays.asList("png", "jpeg","emf","wmf","pict","dib");
            String typeString = "";
            String imgType = "";
            boolean isImg = false;
            for (String type : imgTypes) {
                typeString = "data:image/" + type + ";base64,";
                if (base64string.contains(typeString)){
                    isImg = true;
                    imgType = type;
                    break;
                }
            }

            if(!isImg){
                return;
            }

            Sheet sheet = workbook.getSheetAt(0);
            // 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            //将图片读到BufferedImage

            BASE64Decoder decoder = new BASE64Decoder();


            base64string = base64string.replace(typeString, "");
            byte[] bytes = decoder.decodeBuffer(base64string);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage bufferImg = ImageIO.read(inputStream);
            // 将图片写入流中
            ImageIO.write(bufferImg, imgType, byteArrayOut);

            Drawing<?> patriarch = sheet.createDrawingPatriarch();
            /*
             * 该构造函数有8个参数
             * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
             * 后四个参数，前2个表示图片左上角所在的cellNum和 rowNum，后两个参数对应的表示图片右下角所在的cellNum和 rowNum，
             * excel中的cellNum和rowNum的index都是从0开始的
             *
             */
            //图片一导出到单元格B2中
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0,
                    (short) startColumn, startRowNum, (short) endColumn, endRowNum);
            // 插入图片
            patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut
                    .toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
        }
    }
}
