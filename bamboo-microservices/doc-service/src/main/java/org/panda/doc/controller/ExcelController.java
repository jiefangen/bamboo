package org.panda.doc.controller;

import org.panda.core.spec.restful.RestfulResult;
import org.panda.doc.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public RestfulResult excelUpload(@RequestPart("excelFile") MultipartFile excelFile) {
        excelService.dataRead();
        return RestfulResult.success();
    }

}
