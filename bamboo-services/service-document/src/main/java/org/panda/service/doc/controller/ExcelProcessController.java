package org.panda.service.doc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.common.utils.DocFileUtils;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.model.excel.ExcelDataEnum;
import org.panda.service.doc.model.excel.SampleExcelFill;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Api(tags = "文件Excel处理")
@RestController
@RequestMapping(value = "/excel/process")
public class ExcelProcessController {

    @Autowired
    private FileProcessService fileProcessService;

    @ApiOperation("Excel文件上传读取")
    @PostMapping(value = "/upload/read", consumes = "multipart/form-data")
    public RestfulResult<?> uploadRead(@RequestPart("excelFile") MultipartFile excelFile,
                                            @RequestParam String tags,
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
        docFileParam.setTags(tags);
        Object result = fileProcessService.excelReadBySheet(excelFile.getInputStream(), docFileParam, false);
        if (result instanceof String) {
            return RestfulResult.failure((String) result);
        } else {
            return RestfulResult.success(result);
        }
    }

    @ApiOperation("Excel文件导出")
    @GetMapping("/export/{fileId}")
    public void export(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        fileProcessService.excelExport(response, fileId);
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
        // 填充数据组装
        List<SampleExcelFill> dataList = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            SampleExcelFill sampleExcel = new SampleExcelFill();
            sampleExcel.setName("izhuyuu");
            sampleExcel.setPhone("phone:112233" + (i+1));
            sampleExcel.setAge(i*10);
            sampleExcel.setNo(i);
            dataList.add(sampleExcel);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("date", LocalDateTime.now());
        map.put("total", dataList.size());
        fileProcessService.exportFill(response, fileId, dataList, map);
    }
}
