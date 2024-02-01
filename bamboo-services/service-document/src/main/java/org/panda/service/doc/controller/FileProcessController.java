package org.panda.service.doc.controller;

import io.swagger.annotations.Api;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.model.param.DocFileParam;
import org.panda.service.doc.service.FileProcessService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/upload/import")
    public RestfulResult uploadImport(@RequestBody DocFileParam docFileParam) {
        byte[] decodedBytes = Base64.getDecoder().decode(docFileParam.getFileBase64());
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        DocFile docFile = new DocFile();
        docFile.setFilename(docFileParam.getFilename());
        docFile.setFileType(docFileParam.getFileType());
        docFile.setFileSize(docFileParam.getFileSize());
        Object result = fileProcessService.importFle(docFile, inputStream);
        if (result instanceof Long) {
            return RestfulResult.success(result);
        } else {
            return RestfulResult.failure((String) result);
        }
    }

    @GetMapping("/export")
    public void export(@RequestParam Long fileId, HttpServletResponse response) throws IOException {
        DocFile docFile = new DocFile();
        docFile.setId(fileId);
        fileProcessService.fileExport(docFile, response);
    }

}
