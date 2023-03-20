package org.panda.doc.controller;

import org.panda.bamboo.common.constant.StringsConstant;
import org.panda.bamboo.common.util.UUIDUtil;
import org.panda.core.spec.restful.RestfulResult;
import org.panda.doc.common.DocConstant;
import org.panda.doc.common.DocUtil;
import org.panda.doc.common.WebUtil;
import org.panda.doc.model.domain.ExcelModel;
import org.panda.doc.service.ExcelService;
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
        String filename = UUIDUtil.randomUUID8() + StringsConstant.DOT + DocConstant.EXCEL_XLSX;
        excelModel.setFilename(filename);
        WebUtil.setFileResponse(response, filename);
        excelService.excelExport(excelModel, response.getOutputStream());
    }

}
