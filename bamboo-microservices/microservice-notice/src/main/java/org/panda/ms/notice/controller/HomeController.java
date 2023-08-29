package org.panda.ms.notice.controller;

import io.swagger.annotations.Api;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "微服务问候语")
@RestController
@RequestMapping(value = "/home")
public class HomeController {
    @GetMapping
    public RestfulResult<String> home() {
        return RestfulResult.success("The notice microservice");
    }
}
