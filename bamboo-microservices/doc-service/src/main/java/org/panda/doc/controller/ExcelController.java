package org.panda.doc.controller;

import org.panda.core.spec.restful.RestfulResult;
import org.panda.doc.common.DocUtil;
import org.panda.doc.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping(value = "/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping(value = "/upload/mock", consumes = "multipart/form-data")
    public RestfulResult excelUploadMock(@RequestPart("excelFile") MultipartFile excelFile) throws IOException {
        InputStream inputStream = excelFile.getInputStream();
        String fileExtension = DocUtil.getExtension(excelFile.getOriginalFilename());

        Map<String, Object> excelContent = excelService.readExcel(inputStream, fileExtension);
        if (excelContent == null || excelContent.isEmpty()) {
            return RestfulResult.failure();
        }
        return RestfulResult.success(excelContent);
    }

}
