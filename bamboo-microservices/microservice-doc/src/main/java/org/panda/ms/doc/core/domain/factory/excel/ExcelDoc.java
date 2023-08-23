package org.panda.ms.doc.core.domain.factory.excel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.ms.doc.common.DocConstants;
import org.panda.ms.doc.core.domain.model.DocModel;
import org.panda.ms.doc.core.domain.model.ExcelModel;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel文档
 */
public class ExcelDoc implements Excel {

    @Override
    public Map<String, Object> read(InputStream inputStream, String extension) {
        Map<String, Object> contentMap = new LinkedHashMap<>();
        try {
            if (DocConstants.EXCEL_XLS.equalsIgnoreCase(extension)) {
                HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    contentHandle(contentMap, sheet);
                }
                workbook.close();
            } else if (DocConstants.EXCEL_XLSX.equalsIgnoreCase(extension)) {
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    contentHandle(contentMap, sheet);
                }
                workbook.close();
            }
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
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
            if (sheet.getRow(i) != null) {
                columnCount = Math.max(columnCount, sheet.getRow(i).getLastCellNum());
            }
        }
        // 创建一个二维数组
        String[][] excelData = new String[rowCount][columnCount];

        // 读取工作表中的数据到二维数组中
        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < columnCount; j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        excelData[i][j] = getCellValueAsString(cell);
                    } else {
                        excelData[i][j] = Strings.EMPTY_OBJ;
                    }
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
                DecimalFormat decimalFormat = new DecimalFormat("#.########");
                String formattedNumber = decimalFormat.format(cell.getNumericCellValue());
                return formattedNumber;
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.toString();
            default:
                return Strings.EMPTY_OBJ;
        }
    }

    @Override
    public void create(DocModel docModel, ServletOutputStream outputStream) {
        try {
            ExcelModel excelModel = (ExcelModel) docModel;
            Workbook workbook = WorkbookFactory.create(true);
            String content = excelModel.getContent();
            if (StringUtils.isNotBlank(content)) {
                Map<String, Object> contentMap = JsonUtil.json2Map(content);
                for (Map.Entry<String, Object> entry : contentMap.entrySet()) {
                    Sheet sheet = workbook.createSheet(entry.getKey());
                    Object value = entry.getValue();
                    if (value instanceof List) {
                        List<List<String>> valueList = (List<List<String>>) value;
                        for (int i = 0; i < valueList.size(); i++) {
                            List<String> rowList = valueList.get(i);
                            Row dataRow = sheet.createRow(i);
                            for (int j = 0; j < rowList.size(); j++) {
                                dataRow.createCell(j).setCellValue(rowList.get(j));
                            }
                        }
                    }
                }
            } else {
                workbook.createSheet(DocConstants.DEFAULT_SHEET_NAME);
            }
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

    @Override
    public void preview(ServletOutputStream outputStream) {

    }

}
