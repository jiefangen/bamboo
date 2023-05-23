package org.panda.business.official.modules.portal.api.controller;

import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/home")
public class HomeController {
    @GetMapping
    @ConfigAnonymous
    public RestfulResult<String> home() {
        return RestfulResult.success("The business official");
    }

}
