package org.panda.doc.core.excel;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.panda.bamboo.common.constant.StringsConstant;
import org.panda.doc.common.DocConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel文档
 */
public class ExcelDoc implements Excel {

    @Override
    public Map<String, Object> imports(InputStream inputStream, String extension) {
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
            // do nothing
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
    public void exports() {
    }

}
