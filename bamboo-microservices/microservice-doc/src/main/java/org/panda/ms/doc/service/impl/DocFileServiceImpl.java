package org.panda.ms.doc.service.impl;

import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.ms.doc.common.DocConstants;
import org.panda.ms.doc.common.DocExceptionCodes;
import org.panda.ms.doc.common.util.DocUtil;
import org.panda.ms.doc.core.DocumentSupport;
import org.panda.ms.doc.core.domain.factory.excel.Excel;
import org.panda.ms.doc.core.domain.factory.ppt.Ppt;
import org.panda.ms.doc.core.domain.factory.word.Word;
import org.panda.ms.doc.core.domain.model.ExcelModel;
import org.panda.ms.doc.model.entity.DocFile;
import org.panda.ms.doc.repository.DocFileRepo;
import org.panda.ms.doc.service.DocFileService;
import org.panda.tech.core.crypto.md5.Md5Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DocFileServiceImpl extends DocumentSupport implements DocFileService {

    private Excel excelDoc = super.getExcelDoc();
    private Word wordDoc = super.getWordDoc();
    private Ppt pptDoc = super.getPptDoc();

    @Autowired
    private DocFileRepo docFileRepo;

    @Override
    public Object uploadExcel(DocFile docFile, InputStream inputStream) {
        String fileType = docFile.getFileType();
        if (!DocConstants.EXCEL_XLSX.equalsIgnoreCase(fileType) && !DocConstants.EXCEL_XLS.equalsIgnoreCase(fileType)) {
            return DocUtil.getError(DocExceptionCodes.TYPE_NOT_SUPPORT);
        }
        Md5Encryptor encryptor = new Md5Encryptor();
        docFile.setFileMd5(encryptor.encrypt(docFile));
        Object excelContent = excelDoc.read(inputStream, docFile.getFileType());
        docFile.setContent(JsonUtil.toJson(excelContent));
        docFile.setCategory(DocConstants.EXCEL);
        DocFile file = this.save(docFile);
        if (file != null) {
            return file.getId();
        }
        return Commons.RESULT_FAILURE;
    }

    private DocFile save(DocFile docFile) {
        docFile.setAccessibility(true);
        LocalDateTime currentTime = LocalDateTime.now();
        docFile.setCreateTime(currentTime);
        docFile.setUpdateTime(currentTime);
        return docFileRepo.save(docFile);
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
