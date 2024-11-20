package org.panda.service.doc.controller;

import io.swagger.annotations.Api;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.common.utils.DocFileUtils;
import org.panda.service.doc.model.excel.QuotaExcelData;
import org.panda.service.doc.model.param.ExcelDocFileParam;
import org.panda.service.doc.service.FileProcessService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = "文件Excel处理")
@RestController
@RequestMapping(value = "/excel/process")
public class ExcelProcessController {

    @Autowired
    private FileProcessService fileProcessService;

    @PostMapping(value = "/upload/read/quota", consumes = "multipart/form-data")
    public RestfulResult<?> uploadReadQuota(@RequestPart("excelFile") MultipartFile excelFile,
                                            @RequestParam(required = false) String sheetName) throws IOException {
        String filename = excelFile.getOriginalFilename();
        String fileExtension = DocFileUtils.getExtension(filename);
        // 文件基础信息组装
        ExcelDocFileParam docFileParam = new ExcelDocFileParam();
        docFileParam.setFilename(filename);
        docFileParam.setFileType(fileExtension);
        docFileParam.setFileSize(excelFile.getSize());
        docFileParam.setTags(DocConstants.EXCEL_QUOTA_TAGS);
        docFileParam.setSheetName(sheetName);
        // 设定Excel解析数据模型
        Class<QuotaExcelData> dataClass = QuotaExcelData.class;
        Object result = fileProcessService.excelReadBySheet(excelFile.getInputStream(), docFileParam, dataClass, false);
        if (result instanceof String) {
            return RestfulResult.failure((String) result);
        } else {
            return RestfulResult.success(result);
        }
    }

    @GetMapping("/export/quota/{fileId}")
    public void exportQuota(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        // 设定Excel解析数据模型
        Class<QuotaExcelData> dataClass = QuotaExcelData.class;
        fileProcessService.excelExport(response, fileId, dataClass, DocConstants.EXCEL_QUOTA_TAGS);
    }
}
