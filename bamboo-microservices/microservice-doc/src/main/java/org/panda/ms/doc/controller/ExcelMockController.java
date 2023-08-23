package org.panda.ms.doc.controller;

import io.swagger.annotations.Api;
import org.panda.ms.doc.common.util.DocUtil;
import org.panda.ms.doc.model.entity.DocFile;
import org.panda.ms.doc.service.DocExcelService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Api(tags = "Excel文档处理模拟")
@RestController
@RequestMapping(value = "/excel/mock")
public class ExcelMockController {

    @Autowired
    private DocExcelService excelService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public RestfulResult upload(@RequestPart("excelFile") MultipartFile excelFile) throws IOException {
        String filename = excelFile.getOriginalFilename();
        String fileExtension = DocUtil.getExtension(filename);
        InputStream inputStream = excelFile.getInputStream();
        DocFile docFile = new DocFile();
        docFile.setFilename(filename);
        docFile.setFileType(fileExtension);
        docFile.setFileSize(excelFile.getSize());
        Map<String, Object> excelContent = excelService.uploadExcel(docFile, inputStream);
        if (excelContent == null || excelContent.isEmpty()) {
            return RestfulResult.failure();
        }
        return RestfulResult.success(excelContent);
    }

}
