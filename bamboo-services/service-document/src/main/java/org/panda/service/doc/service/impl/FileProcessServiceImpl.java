package org.panda.service.doc.service.impl;

import org.apache.commons.io.IOUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.common.DocExceptionCodes;
import org.panda.service.doc.common.utils.DocFileUtils;
import org.panda.service.doc.common.utils.DocumentUtils;
import org.panda.service.doc.core.DocumentFactoryProducer;
import org.panda.service.doc.core.domain.document.DocModel;
import org.panda.service.doc.core.domain.factory.excel.Excel;
import org.panda.service.doc.core.domain.factory.pdf.Pdf;
import org.panda.service.doc.core.domain.factory.ppt.Ppt;
import org.panda.service.doc.core.domain.factory.word.Word;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.model.excel.ExcelDataEnum;
import org.panda.service.doc.model.param.DocFileParam;
import org.panda.service.doc.model.param.ExcelDocFileParam;
import org.panda.service.doc.repository.DocFileRepo;
import org.panda.service.doc.service.DocFileStorageService;
import org.panda.service.doc.service.FileProcessService;
import org.panda.tech.core.crypto.md5.Md5Encryptor;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FileProcessServiceImpl implements FileProcessService {
    // 具体文档文件工厂实例
    private final Excel excelDoc = DocumentFactoryProducer.getExcelDoc();
    private final Word wordDoc = DocumentFactoryProducer.getWordDoc();
    private final Ppt pptDoc = DocumentFactoryProducer.getPptDoc();
    private final Pdf pdfDoc = DocumentFactoryProducer.getPdfDoc();

    @Autowired
    private DocFileStorageService fileStorageService;
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

    @SuppressWarnings("unchecked")
    @Override
    public Object importFile(DocFileParam docFileParam, InputStream inputStream, boolean docVerify) {
        String fileType = docFileParam.getFileType();
        if (!DocConstants.checkFileType(fileType)) {
            return DocumentUtils.getError(DocExceptionCodes.TYPE_NOT_SUPPORT);
        }
        // 文件MD5验证
        DocFile docFile = new DocFile();
        docFile.setFilename(docFileParam.getFilename());
        docFile.setFileType(docFileParam.getFileType());
        docFile.setFileSize(docFileParam.getFileSize());
        if (docFileVerify(docFile, docVerify)) {
            return DocumentUtils.getError(DocExceptionCodes.FILE_EXISTS);
        }
        docFile.setTags(docFileParam.getTags());

        Object content = readDocument(inputStream, docFile);
        docFile.setContent(JsonUtil.toJson(content));
        // 数据保存入库
        DocFile docFileRes = save(docFile);
        Long docFileId = docFileRes.getId();
        if (DocConstants.EXCEL_XLSX.equalsIgnoreCase(fileType) || DocConstants.EXCEL_XLS.equalsIgnoreCase(fileType)) {
            // 异步保存到EXCEL数据表
            fileStorageService.saveExcelDataAsync(docFileId, (Map<String, Object>)content);
        }
        // 异步保存原文件资源
        fileStorageService.saveFileAsync(docFileId, docFileParam.getFileBytes());
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
    public void fileExport(Long fileId, HttpServletResponse response) throws IOException {
        DocFile docFile = new DocFile();
        docFile.setId(fileId);
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

    private boolean docFileVerify(DocFile docFile, boolean docVerify) {
        Md5Encryptor encryptor = new Md5Encryptor();
        docFile.setFileMd5(encryptor.encrypt(docFile));
        if (docVerify) { // 文件上传MD5验证
            Example<DocFile> example = Example.of(docFile);
            return docFileRepo.count(example) > 0;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Object excelReadBySheet(InputStream inputStream, ExcelDocFileParam docFileParam, ExcelDataEnum dataEnum,
                                       boolean docVerify) {
        if (!DocConstants.checkExcelFileType(docFileParam.getFileType())) {
            return DocumentUtils.getError(DocExceptionCodes.TYPE_NOT_SUPPORT);
        }
        // 文件信息MD5验证
        DocFile docFile = new DocFile();
        docFile.setFilename(docFileParam.getFilename());
        docFile.setFileType(docFileParam.getFileType());
        docFile.setFileSize(docFileParam.getFileSize());
        if (docFileVerify(docFile, docVerify)) {
            return DocumentUtils.getError(DocExceptionCodes.FILE_EXISTS);
        }
        try {
            Class<T> dataClass = (Class<T>) dataEnum.getClazz();
            Map<String, List<T>> contentRes = excelDoc.readByEasyExcel(inputStream, docFileParam.getSheetName(), dataClass);
            // 文档文件数据保存入库
            docFile.setContent(JsonUtil.toJson(contentRes));
            docFile.setCategory(DocConstants.EASY_EXCEL);
            docFile.setTags(dataEnum.getTags());
            DocFile docFileRes = this.save(docFile);
            // 异步保存原文件资源
            fileStorageService.saveFileAsync(docFileRes.getId(), docFileParam.getFileBytes());
            return docFileRes.getId();
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            return e.getMessage();
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void excelExport(HttpServletResponse response, Long fileId, ExcelDataEnum dataEnum) throws IOException {
        Optional<DocFile> docFileOptional = docFileRepo.findById(fileId);
        if (docFileOptional.isPresent()) {
            DocFile docFile = docFileOptional.get();
            String tags = dataEnum.getTags();
            tags = tags == null ? Strings.EMPTY : tags;
            if (!docFile.getAccessibility() || !tags.equals(docFile.getTags())) {
                WebHttpUtil.buildJsonResponse(response, DocumentUtils.getError(DocExceptionCodes.CAN_NOT_LOAD));
                return;
            }
            // 存储的json字符串转换映射对象集
            String content = docFile.getContent();
            Map<String, List<T>> dataMap = new LinkedHashMap<>();
            Map<String, Object> contentMap = JsonUtil.json2Map(content);
            Class<T> dataClass = (Class<T>) dataEnum.getClazz();
            if (!contentMap.isEmpty()) {
                for (Map.Entry<String, Object> entry : contentMap.entrySet()) {
                    String valueJson = JsonUtil.toJson(entry.getValue());
                    List<T> dataList = JsonUtil.json2List(valueJson, dataClass);
                    dataMap.put(entry.getKey(), dataList);
                }
            }
            String filename = DocFileUtils.appendFilename(docFile.getFilename(), Strings.UNDERLINE + tags);
            WebHttpUtil.buildFileResponse(response, filename);
            excelDoc.writeByEasyExcel(response.getOutputStream(), dataMap, dataClass);
            return;
        }
        WebHttpUtil.buildJsonResponse(response, RestfulResult.failure());
    }
}
