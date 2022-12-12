package org.panda.admin.controller;

import org.panda.core.standard.RestfulResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "home")
public class HomeController {
    @GetMapping
    public RestfulResult<String> home() {
        return RestfulResult.success("Background management system.");
    }
}
