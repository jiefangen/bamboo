package org.panda.sms.controller;

import org.panda.core.spec.restful.RestfulResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "home")
public class HomeController {
    @GetMapping
    public RestfulResult<String> home() {
        return RestfulResult.success("The SMS Service");
    }
}
