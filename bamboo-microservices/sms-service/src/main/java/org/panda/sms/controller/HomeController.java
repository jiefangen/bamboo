package org.panda.sms.controller;

import org.panda.bamboo.common.domain.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "home")
public class HomeController {
    @GetMapping
    public Result<String> home() {
        return Result.success("The SMS server");
    }
}
