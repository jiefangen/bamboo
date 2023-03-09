package org.panda.doc.controller;

import org.panda.core.spec.restful.RestfulResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/excel")
public class ExcelController {

    @GetMapping
    public RestfulResult<String> excel() {
        return RestfulResult.success("Excel");
    }

}
