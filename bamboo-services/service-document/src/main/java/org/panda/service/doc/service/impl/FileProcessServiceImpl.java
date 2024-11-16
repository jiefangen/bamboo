package org.panda.service.doc.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.common.DocExceptionCodes;
import org.panda.service.doc.common.utils.DocumentUtils;
import org.panda.service.doc.core.DocumentFactoryProducer;
import org.panda.service.doc.core.domain.document.DocModel;
import org.panda.service.doc.core.domain.factory.excel.Excel;
import org.panda.service.doc.core.domain.factory.pdf.Pdf;
import org.panda.service.doc.core.domain.factory.ppt.Ppt;
import org.panda.service.doc.core.domain.factory.word.Word;
import org.panda.service.doc.model.excel.QuotaExcelData;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.repository.DocFileRepo;
import org.panda.service.doc.service.DocExcelDataService;
import org.panda.service.doc.service.FileProcessService;
import org.panda.tech.core.crypto.md5.Md5Encryptor;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class FileProcessServiceImpl implements FileProcessService {

    private final Excel excelDoc = DocumentFactoryProducer.getExcelDoc();
    private final Word wordDoc = DocumentFactoryProducer.getWordDoc();
    private final Ppt pptDoc = DocumentFactoryProducer.getPptDoc();
    private final Pdf pdfDoc = DocumentFactoryProducer.getPdfDoc();

    @Autowired
    private DocExcelDataService docExcelDataService;
    @Autowired
    private DocFileRepo docFileRepo;

    private Object readDocument(InputStream inputStream, DocFile docFile) {
        String fileType = docFile.getFileType();
        if (DocConstants.EXCEL_XLSX.equalsIgnoreCase(fileType) || DocConstants.EXCEL_XLS.equalsIgnoreCase(fileType)) {
//            docFile.setCategory(DocConstants.EXCEL);
//            return excelDoc.read(inputStream, fileType);
            docFile.setCategory(DocConstants.EASY_EXCEL);
            return excelDoc.readByEasyExcel(inputStream);
        } else if (DocConstants.WORD_DOCX.equalsIgnoreCase(fileType) || DocConstants.WORD_DOC.equalsIgnoreCase(fileType)) {
            docFile.setCategory(DocConstants.WORD);
            return wordDoc.read(inputStream, fileType);
        } else if (DocConstants.PPT_PPTX.equalsIgnoreCase(fileType) || DocConstants.PPT_PPT.equalsIgnoreCase(fileType)) {
            docFile.setCategory(DocConstants.PPT);
            return pptDoc.read(inputStream, fileType);
        } else if (DocConstants.PDF.equalsIgnoreCase(fileType)) {
            docFile.setCategory(DocConstants.PDF);
            return pdfDoc.read(inputStream, fileType);
        }
        return new Object();
    }

    @Override
    public Object importFle(DocFile docFile, InputStream inputStream, boolean md5Verify) {
        String fileType = docFile.getFileType();
        if (!DocConstants.checkFileType(fileType)) {
            return DocumentUtils.getError(DocExceptionCodes.TYPE_NOT_SUPPORT);
        }
        Md5Encryptor encryptor = new Md5Encryptor();
        String fileMd5 = encryptor.encrypt(docFile);
        if (md5Verify) { // 文件上传MD5验证
            DocFile docFileParams = new DocFile();
            docFileParams.setFileMd5(fileMd5);
            Example<DocFile> example = Example.of(docFileParams);
            if (docFileRepo.count(example) > 0) {
                return DocumentUtils.getError(DocExceptionCodes.FILE_EXISTS);
            }
        }
        docFile.setFileMd5(fileMd5);
        Object content = readDocument(inputStream, docFile);
        docFile.setContent(JsonUtil.toJson(content));
        // 数据保存入库
        DocFile docFileRes = save(docFile);
        if (DocConstants.EXCEL_XLSX.equalsIgnoreCase(fileType) || DocConstants.EXCEL_XLS.equalsIgnoreCase(fileType)) {
            // 异步保存到EXCEL数据表
            docExcelDataService.saveExcelDataAsync(docFileRes.getId(), (Map<String, Object>)content);
        }
        return docFileRes;
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
        docFile.setAccessibility(true);
        Example<DocFile> example = Example.of(docFile);
        Optional<DocFile> docFileOptional = docFileRepo.findOne(example);
        if (docFileOptional.isPresent()) {
            DocFile file = docFileOptional.get();
            DocModel docModel = new DocModel();
            String filename = file.getFilename();
            docModel.setFilename(filename);
            String fileType = file.getFileType();
            docModel.setFileType(fileType);
            docModel.setContent(file.getContent());
            WebHttpUtil.buildFileResponse(response, filename);
            if (DocConstants.EXCEL_XLSX.equalsIgnoreCase(fileType) || DocConstants.EXCEL_XLS.equalsIgnoreCase(fileType)) {
                excelDoc.create(response.getOutputStream(), docModel);
            } else if (DocConstants.WORD_DOCX.equalsIgnoreCase(fileType) || DocConstants.WORD_DOC.equalsIgnoreCase(fileType)) {
                wordDoc.create(response.getOutputStream(), docModel);
            } else if (DocConstants.PPT_PPTX.equalsIgnoreCase(fileType) || DocConstants.PPT_PPT.equalsIgnoreCase(fileType)) {
                pptDoc.create(response.getOutputStream(), docModel);
            } else {
                pdfDoc.create(response.getOutputStream(), docModel);
            }
        }
    }

    @Override
    public Object excelReadBySheet(InputStream inputStream, String sheetName, String fileExtension) {
        if (!DocConstants.checkExcelFileType(fileExtension)) {
            return DocumentUtils.getError(DocExceptionCodes.TYPE_NOT_SUPPORT);
        }
        try {
            if (StringUtils.isEmpty(sheetName)) {
                return excelDoc.readByEasyExcel(inputStream, QuotaExcelData.class);
            } else {
                return excelDoc.readByEasyExcel(inputStream, sheetName, QuotaExcelData.class);
            }
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            return e.getMessage();
        }
    }
}
