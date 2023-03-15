package org.panda.doc.core.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.panda.doc.common.DocConstant;

import java.io.IOException;
import java.io.InputStream;

/**
 * Excel文档
 */
public class ExcelDoc implements Excel {

    @Override
    public String read(InputStream inputStream, String fileExtension) throws IOException {
        StringBuilder content = new StringBuilder();
        if (DocConstant.EXCEL_XLS.equalsIgnoreCase(fileExtension)) {
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    content.append(cell.toString()).append("\t");
                }
                content.append("\n");
            }
            workbook.close();
        } else if (DocConstant.EXCEL_XLSX.equalsIgnoreCase(fileExtension)) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    content.append(cell.toString()).append("\t");
                }
                content.append("\n");
            }
            workbook.close();
        }
        return content.toString();
    }

    @Override
    public void convert() {
    }

}
