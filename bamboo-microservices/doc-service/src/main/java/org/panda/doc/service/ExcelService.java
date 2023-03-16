package org.panda.doc.service;

import org.panda.doc.common.DocConstant;
import org.panda.doc.core.DocFactory;
import org.panda.doc.core.excel.Excel;
import org.panda.doc.core.model.ExcelModel;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Excel文档操作服务
 *
 * @author fangen
 **/
@Service
public class ExcelService {

    private Excel excelDoc = (Excel) DocFactory.getDocument(DocConstant.EXCEL);

    public Map<String, Object> readExcel(InputStream inputStream, String fileExtension) {
        Map<String, Object> excelContent = (Map<String, Object>) excelDoc.read(inputStream, fileExtension);
        return excelContent;
    }

    public void excelExport(ServletOutputStream outputStream) {
        ExcelModel excelModel = new ExcelModel();

        excelDoc.create(excelModel, outputStream);
    }

}
