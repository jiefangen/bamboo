package org.panda.doc.core.excel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.panda.bamboo.common.constant.StringsConstant;
import org.panda.doc.common.DocConstant;
import org.panda.doc.model.domain.DocModel;
import org.panda.doc.model.domain.ExcelModel;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel文档
 */
public class ExcelDoc implements Excel {

    @Override
    public Map<String, Object> read(InputStream inputStream, String extension) {
        Map<String, Object> contentMap = new LinkedHashMap<>();
        try {
            if (DocConstant.EXCEL_XLS.equalsIgnoreCase(extension)) {
                HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    contentHandle(contentMap, sheet);
                }
                workbook.close();
            } else if (DocConstant.EXCEL_XLSX.equalsIgnoreCase(extension)) {
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    contentHandle(contentMap, sheet);
                }
                workbook.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return contentMap;
    }

    private void contentHandle(Map<String, Object> contentMap, Sheet sheet) {
        // 获取工作表中的行数和列数
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
        int columnCount = 0;
        for (int i = 0; i < rowCount; i++) {
            columnCount = Math.max(columnCount, sheet.getRow(i).getLastCellNum());
        }
        // 创建一个二维数组
        String[][] excelData = new String[rowCount][columnCount];

        // 读取工作表中的数据到二维数组中
        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < columnCount; j++) {
                Cell cell = row.getCell(j);
                if (cell != null) {
                    excelData[i][j] = getCellValueAsString(cell);
                } else {
                    excelData[i][j] = StringsConstant.EMPTY;
                }
            }
        }
        contentMap.put(sheet.getSheetName(), excelData);
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.toString();
            default:
                return StringsConstant.EMPTY;
        }
    }

    @Override
    public void create(DocModel docModel, ServletOutputStream outputStream) {
        try {
            ExcelModel excelModel = (ExcelModel) docModel;
            String sheetName = excelModel.getSheetName();
            if (StringUtils.isEmpty(sheetName)) {
                sheetName = DocConstant.DEFAULT_SHEET_NAME;
            }
            Workbook workbook = WorkbookFactory.create(true);
            Sheet sheet = workbook.createSheet(sheetName);

            // 创建表头
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("姓名");
            headerRow.createCell(1).setCellValue("年龄");
            headerRow.createCell(2).setCellValue("性别");

            // 创建数据行
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("张三");
            dataRow.createCell(1).setCellValue(20);
            dataRow.createCell(2).setCellValue("男");

            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void preview(ServletOutputStream outputStream) {

    }

}
