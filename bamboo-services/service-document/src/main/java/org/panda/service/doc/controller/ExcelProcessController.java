package org.panda.service.doc.controller;

import io.swagger.annotations.Api;
import org.panda.service.doc.common.utils.DocumentUtils;
import org.panda.service.doc.model.param.DocFileParam;
import org.panda.service.doc.service.FileProcessService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "Excel文件处理")
@RestController
@RequestMapping(value = "/excel/process")
public class ExcelProcessController {

    @Autowired
    private FileProcessService fileProcessService;

    @PostMapping(value = "/excel/read", consumes = "multipart/form-data")
    public RestfulResult<?> excelRead(@RequestPart("excelFile") MultipartFile excelFile,
                                      @RequestParam(required = false) String sheetName) throws IOException {
        String filename = excelFile.getOriginalFilename();
        String fileExtension = DocumentUtils.getExtension(filename);
        // 文件基础信息组装
        DocFileParam docFileParam = new DocFileParam();
        docFileParam.setFilename(filename);
        docFileParam.setFileType(fileExtension);
        docFileParam.setFileSize(excelFile.getSize());
        Object result = fileProcessService.excelReadBySheet(excelFile.getInputStream(), docFileParam, sheetName);
        if (result instanceof String) {
            return RestfulResult.failure((String) result);
        } else {
            return RestfulResult.success(result);
        }
    }
}
