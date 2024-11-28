package org.panda.service.doc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.common.utils.DocFileUtils;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.model.excel.ExcelDataEnum;
import org.panda.service.doc.model.param.DocFileParam;
import org.panda.service.doc.model.param.ExcelDocFileParam;
import org.panda.service.doc.service.FileProcessService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Api(tags = "文件Excel处理")
@RestController
@RequestMapping(value = "/excel/process")
public class ExcelProcessController {

    // 设定额度数据模型枚举
    private final ExcelDataEnum dataEnum = ExcelDataEnum.QUOTA_DATA;

    @Autowired
    private FileProcessService fileProcessService;

    @ApiOperation("Excel文件上传读取")
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
        docFileParam.setSheetName(sheetName);
        docFileParam.setFileBytes(excelFile.getBytes());
        Object result = fileProcessService.excelReadBySheet(excelFile.getInputStream(), docFileParam, dataEnum, false);
        if (result instanceof String) {
            return RestfulResult.failure((String) result);
        } else {
            return RestfulResult.success(result);
        }
    }

    @ApiOperation("Excel文件导出")
    @GetMapping("/export/quota/{fileId}")
    public void exportQuota(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        fileProcessService.excelExport(response, fileId, dataEnum);
    }

    @ApiOperation("Excel文件模版上传")
    @PostMapping(value = "/upload/template", consumes = "multipart/form-data")
    public RestfulResult<?> uploadExcelTemplate(@RequestPart("excelTemplate") MultipartFile excelTemplate,
                                                @RequestParam String tags) throws IOException {
        if (ExcelDataEnum.getExelEnumByTags(tags) == null) {
            return RestfulResult.failure("The Excel file template tags is invalid.");
        }
        DocFileParam docFileParam = new DocFileParam();
        docFileParam.setTags(tags);
        docFileParam.setBizAttributes(DocConstants.BIZ_TEMPLATE);
        DocFileUtils.transformDocFile(excelTemplate, docFileParam);

        InputStream inputStream = excelTemplate.getInputStream();
        Object result = fileProcessService.importFile(docFileParam, inputStream, true);
        if (result instanceof DocFile) {
            DocFile docFileRes = (DocFile) result;
            return RestfulResult.success(docFileRes.getId());
        }
        return RestfulResult.failure((String) result);
    }

    @ApiOperation("导出填充Excel文件")
    @GetMapping("/export/fill/{fileId}")
    public void exportFill(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        fileProcessService.exportFill(response, fileId);
    }
}
