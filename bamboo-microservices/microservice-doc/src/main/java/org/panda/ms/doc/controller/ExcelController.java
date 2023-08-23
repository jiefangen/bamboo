package org.panda.ms.doc.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.UUIDUtil;
import org.panda.ms.doc.common.DocConstants;
import org.panda.ms.doc.common.util.WebUtil;
import org.panda.ms.doc.model.entity.DocFile;
import org.panda.ms.doc.model.param.DocFileParam;
import org.panda.ms.doc.service.DocExcelService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

@Api(tags = "Excel文档处理")
@RestController
@RequestMapping(value = "/excel")
public class ExcelController {

    @Autowired
    private DocExcelService excelService;

    @PostMapping(value = "/upload")
    public RestfulResult upload(@RequestBody DocFileParam docFileParam) {
        byte[] decodedBytes = Base64.getDecoder().decode(docFileParam.getFileBase64());
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        DocFile docFile = new DocFile();
        docFile.setFilename(docFileParam.getFilename());
        docFile.setFileType(docFileParam.getFileType());
        Map<String, Object> excelContent = excelService.uploadExcel(docFile, inputStream);
        if (excelContent == null || excelContent.isEmpty()) {
            return RestfulResult.failure();
        }
        return RestfulResult.success(excelContent);
    }

    @GetMapping("/download")
    public void download(@RequestParam Long fileId, HttpServletResponse response) throws IOException {
        String filename = UUIDUtil.randomUUID8() + Strings.DOT + DocConstants.EXCEL_XLSX;
        WebUtil.setFileResponse(response, filename);
        DocFile docFile = new DocFile();
        docFile.setId(fileId);
        excelService.excelExport(docFile, response.getOutputStream());
    }

}
