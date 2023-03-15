package org.panda.doc.service;

import org.panda.doc.common.DocConstant;
import org.panda.doc.core.DocFactory;
import org.panda.doc.core.excel.Excel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Excel文档操作服务
 *
 * @author fangen
 **/
@Service
public class ExcelService {

    private Excel excel = (Excel) DocFactory.getDocument(DocConstant.EXCEL);

    public String readExcel(InputStream inputStream, String fileExtension) {
        try {
            String excelContent = excel.read(inputStream, fileExtension);
            return excelContent;
        } catch (IOException e) {
            // do nothing
        }
        return null;
    }

}
