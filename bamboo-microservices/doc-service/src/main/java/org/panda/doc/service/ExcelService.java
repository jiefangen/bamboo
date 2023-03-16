package org.panda.doc.service;

import org.panda.doc.common.DocConstant;
import org.panda.doc.core.DocFactory;
import org.panda.doc.core.excel.Excel;
import org.springframework.stereotype.Service;

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
        Map<String, Object> excelContent = (Map<String, Object>) excelDoc.imports(inputStream, fileExtension);
        return excelContent;
    }

}
