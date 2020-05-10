package org.panda.modules.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开场白
 *
 * @author jvfagan
 * @since JDK 1.8  2020/5/8
 **/
@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(){
        return "Hello World!";
    }
}
