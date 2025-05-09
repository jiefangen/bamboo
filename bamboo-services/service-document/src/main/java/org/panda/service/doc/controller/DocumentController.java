package org.panda.service.doc.controller;

import io.swagger.annotations.Api;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.model.param.DocFileQueryParam;
import org.panda.service.doc.service.DocFileService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "文档文件数据服务")
@RestController
@RequestMapping(value = "/file/data")
public class DocumentController {

    @Autowired
    private DocFileService docFileService;

    @PostMapping(value = "/doc/page")
    public RestfulResult<QueryResult<DocFile>> documentPage(@RequestBody DocFileQueryParam queryParam) {
        QueryResult<DocFile> docFilePage = docFileService.getDocFileByPage(queryParam);
        return RestfulResult.success(docFilePage);
    }

    @PostMapping(value = "/getDocPage")
    public RestfulResult<QueryResult<DocFile>> getDocPage(@RequestBody DocFileQueryParam queryParam) {
        QueryResult<DocFile> docFilePage = docFileService.getDocument(queryParam);
        return RestfulResult.success(docFilePage);
    }
}
