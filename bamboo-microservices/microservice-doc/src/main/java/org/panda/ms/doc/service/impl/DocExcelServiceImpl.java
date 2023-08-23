package org.panda.ms.doc.service.impl;

import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.ms.doc.common.DocConstants;
import org.panda.ms.doc.core.DocumentSupport;
import org.panda.ms.doc.core.domain.factory.excel.Excel;
import org.panda.ms.doc.core.domain.model.ExcelModel;
import org.panda.ms.doc.model.entity.DocFile;
import org.panda.ms.doc.repository.DocFileRepo;
import org.panda.ms.doc.repository.DocFileRepox;
import org.panda.ms.doc.service.DocExcelService;
import org.panda.ms.doc.service.DocFileService;
import org.panda.tech.core.crypto.md5.Md5Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class DocExcelServiceImpl extends DocumentSupport implements DocExcelService {

    private Excel excelDoc = super.getExcelDoc();

    @Autowired
    private DocFileService docFileService;
    @Autowired
    private DocFileRepo docFileRepo;
    @Autowired
    private DocFileRepox docFileRepox;

    @Override
    public Map<String, Object> uploadExcel(DocFile docFile, InputStream inputStream) {
        // 保存上传Excel文件信息
        Md5Encryptor encryptor = new Md5Encryptor();
        docFile.setFileMd5(encryptor.encrypt(docFile));
        Map<String, Object> excelContent = (Map<String, Object>) excelDoc.read(inputStream, docFile.getFileType());
        docFile.setContent(JsonUtil.toJson(excelContent));
        docFile.setCategory(DocConstants.EXCEL);
        docFileService.save(docFile);
        return excelContent;
    }

    @Override
    public void excelExport(DocFile docFile, ServletOutputStream outputStream) {
        Example<DocFile> example = Example.of(docFile);
        Optional<DocFile> docFileOptional = docFileRepo.findOne(example);
        if (docFileOptional.isPresent()) {
            DocFile file = docFileOptional.get();
            ExcelModel excelModel = new ExcelModel();
            excelModel.setFilename(file.getFilename());
            excelModel.setFileType(file.getFileType());
            excelModel.setContent(file.getContent());
            excelDoc.create(excelModel, outputStream);
        }
    }
}
