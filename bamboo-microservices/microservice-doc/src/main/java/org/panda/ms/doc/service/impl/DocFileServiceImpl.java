package org.panda.ms.doc.service.impl;

import org.panda.bamboo.common.constant.basic.Strings;
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
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DocFileServiceImpl extends DocumentSupport implements DocFileService {

    private final Excel excelDoc = super.getExcelDoc();
    private final Word wordDoc = super.getWordDoc();
    private final Ppt pptDoc = super.getPptDoc();

    @Autowired
    private DocFileRepo docFileRepo;

    @Override
    public Object importFle(DocFile docFile, InputStream inputStream) {
        String fileType = docFile.getFileType();
        if (!DocConstants.checkFileType(fileType)) {
            return DocUtil.getError(DocExceptionCodes.TYPE_NOT_SUPPORT);
        }
        Md5Encryptor encryptor = new Md5Encryptor();
        docFile.setFileMd5(encryptor.encrypt(docFile));
        Object excelContent = Strings.EMPTY;
        if (DocConstants.EXCEL_XLSX.equalsIgnoreCase(fileType) || DocConstants.EXCEL_XLS.equalsIgnoreCase(fileType)) {
            docFile.setCategory(DocConstants.EXCEL);
            excelContent = excelDoc.read(inputStream, docFile.getFileType());
        } else if (DocConstants.WORD_DOCX.equalsIgnoreCase(fileType) || DocConstants.WORD_DOC.equalsIgnoreCase(fileType)) {
            docFile.setCategory(DocConstants.WORD);
            excelContent = wordDoc.read(inputStream, docFile.getFileType());
        } else if (DocConstants.PPT_PPTX.equalsIgnoreCase(fileType) || DocConstants.PPT_PPT.equalsIgnoreCase(fileType)) {
            docFile.setCategory(DocConstants.PPT);
            excelContent = pptDoc.read(inputStream, docFile.getFileType());
        }
        docFile.setContent(JsonUtil.toJson(excelContent));
        // 数据保存入库
        DocFile file = this.save(docFile);
        return file.getId();
    }

    private DocFile save(DocFile docFile) {
        docFile.setAccessibility(true);
        LocalDateTime currentTime = LocalDateTime.now();
        docFile.setCreateTime(currentTime);
        docFile.setUpdateTime(currentTime);
        return docFileRepo.save(docFile);
    }

    @Override
    public void fileExport(DocFile docFile, HttpServletResponse response) throws IOException {
        Example<DocFile> example = Example.of(docFile);
        Optional<DocFile> docFileOptional = docFileRepo.findOne(example);
        if (docFileOptional.isPresent()) {
            DocFile file = docFileOptional.get();
            ExcelModel excelModel = new ExcelModel();
            String filename = file.getFilename();
            excelModel.setFilename(filename);
            String fileType = file.getFileType();
            excelModel.setFileType(fileType);
            excelModel.setContent(file.getContent());
            if (DocConstants.EXCEL_XLSX.equalsIgnoreCase(fileType) || DocConstants.EXCEL_XLS.equalsIgnoreCase(fileType)) {
                excelDoc.create(excelModel, response.getOutputStream());
            } else if (DocConstants.WORD_DOCX.equalsIgnoreCase(fileType) || DocConstants.WORD_DOC.equalsIgnoreCase(fileType)) {
                wordDoc.create(excelModel, response.getOutputStream());
            } else if (DocConstants.PPT_PPTX.equalsIgnoreCase(fileType) || DocConstants.PPT_PPT.equalsIgnoreCase(fileType)) {
                pptDoc.create(excelModel, response.getOutputStream());
            }
            WebHttpUtil.buildFileResponse(response, filename);
        }
    }
}
