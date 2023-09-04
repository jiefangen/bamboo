package org.panda.ms.payment.controller;

import io.swagger.annotations.Api;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "微服务问候语")
@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @GetMapping
    @ResponseBody
    public RestfulResult<String> home() {
        return RestfulResult.success("The payment microservice");
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "The payment microservice");
        return "hello";
    }

}
