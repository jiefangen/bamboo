package org.panda.service.doc.core.domain.factory.excel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.constant.file.FileExtensions;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.common.DocExceptionCodes;
import org.panda.service.doc.common.util.DocUtil;
import org.panda.service.doc.core.domain.factory.excel.helper.ExcelDocHelper;
import org.panda.service.doc.core.domain.factory.excel.helper.ExcelDocxHelper;
import org.panda.service.doc.core.domain.document.DocModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel文档
 */
public class ExcelDoc implements Excel {

    @Override
    public Object read(InputStream inputStream, String extension) {
        Map<String, Object> contentMap = new LinkedHashMap<>();
        try {
            if (DocConstants.EXCEL_XLS.equalsIgnoreCase(extension)) {
                ExcelDocHelper excelDocHelper = new ExcelDocHelper(inputStream);
                HSSFWorkbook workbook = excelDocHelper.getOrigin();
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    contentHandle(contentMap, sheet);
                }
                excelDocHelper.close();
            } else {
                ExcelDocxHelper excelDocxHelper = new ExcelDocxHelper(inputStream);
                XSSFWorkbook workbook = excelDocxHelper.getOrigin();
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    contentHandle(contentMap, sheet);
                }
                excelDocxHelper.close();
            }
        } catch (Exception e) {
            throw new BusinessException(DocUtil.getError(DocExceptionCodes.CAN_NOT_LOAD), FileExtensions.XLSX.toUpperCase());
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
                        excelData[i][j] = Strings.STR_NULL;
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
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("#.########");
                    String formattedNumber = decimalFormat.format(cell.getNumericCellValue());
                    return formattedNumber;
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.toString();
            default:
                return Strings.STR_NULL;
        }
    }

    @Override
    public void create(OutputStream outputStream, DocModel docModel) {
        try {
            Workbook workbook = WorkbookFactory.create(true);
            String content = docModel.getContent();
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
    public void convert(InputStream inputStream, OutputStream outputStream, String extension) {
    }

}
