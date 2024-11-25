package org.panda.service.doc.controller;

import io.swagger.annotations.Api;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.common.utils.DocFileUtils;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.model.param.DocFileParam;
import org.panda.service.doc.service.FileProcessService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Api(tags = "文档文件处理")
@RestController
@RequestMapping(value = "/file/process")
public class FileProcessController {

    @Autowired
    private FileProcessService fileProcessService;

    @PostMapping(value = "/upload/read", consumes = "multipart/form-data")
    public RestfulResult<?> uploadRead(@RequestPart("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fileExtension = DocFileUtils.getExtension(filename);
        InputStream inputStream = file.getInputStream();
        DocFileParam docFileParam = new DocFileParam();
        docFileParam.setFilename(filename);
        docFileParam.setFileType(fileExtension);
        docFileParam.setFileSize(file.getSize());
        docFileParam.setTags(DocConstants.FILE_DOCUMENT_TAGS);
        docFileParam.setFileBytes(file.getBytes());
        Object result = fileProcessService.importFile(docFileParam, inputStream, false);
        if (result instanceof DocFile) {
            DocFile docFileRes = (DocFile) result;
            return RestfulResult.success(docFileRes.getId());
        } else {
            return RestfulResult.failure((String) result);
        }
    }

    @PostMapping(value = "/import")
    public RestfulResult<?> fileImport(@RequestBody DocFileParam docFileParam) {
        byte[] decodedBytes = Base64.getDecoder().decode(docFileParam.getFileBase64());
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        docFileParam.setTags(DocConstants.FILE_DOCUMENT_TAGS);
        docFileParam.setFileBytes(decodedBytes);
        Object result = fileProcessService.importFile(docFileParam, inputStream, true);
        if (result instanceof DocFile) {
            DocFile docFileRes = (DocFile) result;
            return RestfulResult.success(docFileRes.getId());
        } else {
            return RestfulResult.failure((String) result);
        }
    }

    @GetMapping("/export/{fileId}")
    public void fileExport(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        fileProcessService.fileExport(fileId, response);
    }

    @GetMapping("/create/{fileId}")
    public void createFile(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        fileProcessService.createFile(fileId, response);
    }
}
