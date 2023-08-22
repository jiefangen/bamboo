package org.panda.service.doc.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.UUIDUtil;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.common.util.DocUtil;
import org.panda.service.doc.common.util.WebUtil;
import org.panda.service.doc.core.domain.model.ExcelModel;
import org.panda.service.doc.service.DocExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
        excelFile.getName();
        DocFile docFile = new DocFile();
        docFile.setFilename(filename);
        docFile.setFileType(fileExtension);
        docFile.setFileSize(excelFile.getSize());
        Map<String, Object> excelContent = excelService.uploadExcel(inputStream, docFile);
        if (excelContent == null || excelContent.isEmpty()) {
            return RestfulResult.failure();
        }
        return RestfulResult.success(excelContent);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        ExcelModel excelModel = new ExcelModel();
        String filename = UUIDUtil.randomUUID8() + Strings.DOT + DocConstants.EXCEL_XLSX;
        excelModel.setFilename(filename);
        WebUtil.setFileResponse(response, filename);
        excelService.excelExport(excelModel, response.getOutputStream());
    }

}
