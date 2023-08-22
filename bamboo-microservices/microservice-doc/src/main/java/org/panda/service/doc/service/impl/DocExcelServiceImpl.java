package org.panda.service.doc.service.impl;

import org.panda.service.doc.core.DocumentSupport;
import org.panda.service.doc.core.domain.factory.excel.Excel;
import org.panda.service.doc.core.domain.model.ExcelModel;
import org.panda.service.doc.model.entity.DocExcel;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.repository.DocExcelRepo;
import org.panda.service.doc.service.DocExcelService;
import org.panda.service.doc.service.DocFileService;
import org.panda.tech.core.crypto.md5.Md5Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class DocExcelServiceImpl extends DocumentSupport implements DocExcelService {

    @Autowired
    private DocFileService docFileService;
    @Autowired
    private DocExcelRepo docExcelRepo;

    private Excel excelDoc = super.getExcelDoc();

    @Override
    public Map<String, Object> uploadExcel(InputStream inputStream, DocFile docFile) {
        // 保存上传Excel文件信息
        Md5Encryptor encryptor = new Md5Encryptor();
        docFile.setFileMd5(encryptor.encrypt(docFile));
        docFileService.save(docFile);
        Map<String, Object> excelContent = (Map<String, Object>) excelDoc.read(inputStream, docFile.getFileType());
        List<DocExcel> docExcels = new LinkedList<>();
        // 保存文件解析的数据信息
        for (Map.Entry<String, Object> entry : excelContent.entrySet()) {
            DocExcel docExcel = new DocExcel();
//            docExcel.setDocId();

            String sheetName = entry.getKey();
            docExcel.setSheetName(sheetName);

            docExcels.add(docExcel);
        }
//        docExcelRepo.saveAll(docExcels);
        return excelContent;
    }

    @Override
    public void excelExport(ExcelModel excelModel, ServletOutputStream outputStream) {
//        List<DocFile> docFiles = docFileRepo.findAll();
        excelDoc.create(excelModel, outputStream);
    }
}
