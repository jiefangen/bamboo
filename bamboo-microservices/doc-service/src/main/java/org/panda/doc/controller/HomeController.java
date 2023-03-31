package org.panda.doc.controller;

import com.alibaba.fastjson.JSONObject;
import org.panda.core.spec.restful.RestfulResult;
import org.panda.doc.common.entity.DocFile;
import org.panda.doc.dao.DocFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/home")
public class HomeController {
    @Autowired
    private DocFileRepository docFileRepository;

    @GetMapping
    public RestfulResult<String> home() {
        return RestfulResult.success("The doc microservice");
    }

    @GetMapping("/findRepo")
    public RestfulResult<String> findRepo() {
        List<DocFile> docFiles = docFileRepository.findAll();
        return RestfulResult.success(JSONObject.toJSONString(docFiles));
    }

}
