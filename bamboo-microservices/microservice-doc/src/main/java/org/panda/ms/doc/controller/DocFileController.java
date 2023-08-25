package org.panda.ms.doc.controller;

import io.swagger.annotations.Api;
import org.panda.ms.doc.model.entity.DocFile;
import org.panda.ms.doc.model.param.DocFileQueryParam;
import org.panda.ms.doc.service.DocFileService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "文件数据服务")
@RestController
@RequestMapping(value = "/file/data")
public class DocFileController {

    @Autowired
    private DocFileService docFileService;

    @PostMapping(value = "/page")
    public RestfulResult page(@RequestBody DocFileQueryParam queryParam) {
        QueryResult<DocFile> docFilePage = docFileService.getDocFileByPage(queryParam);
        return RestfulResult.success(docFilePage);
    }

}
