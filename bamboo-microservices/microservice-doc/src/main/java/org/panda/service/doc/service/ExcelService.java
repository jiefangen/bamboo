package org.panda.service.doc.service;

import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.common.entity.DocFile;
import org.panda.service.doc.core.DocFactory;
import org.panda.service.doc.core.domain.ExcelModel;
import org.panda.service.doc.core.excel.Excel;
import org.panda.service.doc.dao.DocFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Excel文档操作服务
 *
 * @author fangen
 **/
@Service
public class ExcelService {

    @Autowired
    private DocFileRepo docFileRepo;

    private Excel excelDoc = (Excel) DocFactory.getDocument(DocConstants.EXCEL);

    public Map<String, Object> readExcel(InputStream inputStream, String fileExtension) {
        Map<String, Object> excelContent = (Map<String, Object>) excelDoc.read(inputStream, fileExtension);
        return excelContent;
    }

    public void excelExport(ExcelModel excelModel, ServletOutputStream outputStream) {
        List<DocFile> docFiles = docFileRepo.findAll();

        excelDoc.create(excelModel, outputStream);
    }

}
