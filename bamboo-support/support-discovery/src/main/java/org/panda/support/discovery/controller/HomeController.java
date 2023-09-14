package org.panda.support.discovery.controller;

import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @Value("${spring.application.name}")
    private String name;
    @Value("${spring.profiles.active}")
    private String env;
    @Value("${server.port}")
    private String port;

    @GetMapping
    @ResponseBody
    public RestfulResult<Map<String, String>> home(HttpServletRequest request) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("serviceName", name);
        resultMap.put("env", env);
        resultMap.put("port", port);
        resultMap.put("localHost", NetUtil.getLocalHost());
        resultMap.put("remoteAddress", WebHttpUtil.getRemoteAddress(request));
        return RestfulResult.success(resultMap);
    }

}
