package org.panda.service.doc.controller;

import org.panda.bamboo.common.constant.Strings;
import org.panda.bamboo.common.util.UUIDUtil;
import org.panda.tech.core.spec.restful.RestfulResult;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.common.util.DocUtil;
import org.panda.service.doc.common.util.WebUtil;
import org.panda.service.doc.core.domain.ExcelModel;
import org.panda.service.doc.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
        String fileExtension = DocUtil.getExtension(excelFile.getOriginalFilename());
        InputStream inputStream = excelFile.getInputStream();
        Map<String, Object> excelContent = excelService.readExcel(inputStream, fileExtension);
        if (excelContent == null || excelContent.isEmpty()) {
            return RestfulResult.failure();
        }
        return RestfulResult.success(excelContent);
    }

    @GetMapping("/excelExport/mock")
    public void excelExport(HttpServletResponse response) throws IOException {
        ExcelModel excelModel = new ExcelModel();
        String filename = UUIDUtil.randomUUID8() + Strings.DOT + DocConstants.EXCEL_XLSX;
        excelModel.setFilename(filename);
        WebUtil.setFileResponse(response, filename);
        excelService.excelExport(excelModel, response.getOutputStream());
    }

}
