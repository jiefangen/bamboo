package org.panda.doc.controller;

import org.panda.core.spec.restful.RestfulResult;
import org.panda.core.util.UUIDUtil;
import org.panda.doc.common.DocConstant;
import org.panda.doc.common.DocUtil;
import org.panda.doc.common.WebUtil;
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

    @GetMapping("/excelExport")
    public void excelExport(HttpServletResponse response) throws IOException {
        String filename = UUIDUtil.randomUUID8() + "." + DocConstant.EXCEL_XLSX;
        WebUtil.setFileResponse(response, filename);
        excelService.excelExport(response.getOutputStream());
    }

}
