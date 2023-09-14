package org.panda.service.doc.controller;

import io.swagger.annotations.Api;
import org.panda.service.doc.common.util.DocUtil;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.service.FileProcessService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Api(tags = "文档文件处理模拟")
@RestController
@RequestMapping(value = "/file/process/mock")
public class FileProcessMockController {

    @Autowired
    private FileProcessService fileProcessService;

    @PostMapping(value = "/upload/import", consumes = "multipart/form-data")
    public RestfulResult uploadImport(@RequestPart("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fileExtension = DocUtil.getExtension(filename);
        InputStream inputStream = file.getInputStream();
        DocFile docFile = new DocFile();
        docFile.setFilename(filename);
        docFile.setFileType(fileExtension);
        docFile.setFileSize(file.getSize());
        Object result = fileProcessService.importFle(docFile, inputStream);
        if (result instanceof Long) {
            return RestfulResult.success(result);
        } else {
            return RestfulResult.failure((String) result);
        }
    }

}
