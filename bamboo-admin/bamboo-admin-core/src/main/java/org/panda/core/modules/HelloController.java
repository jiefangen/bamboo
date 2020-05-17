package org.panda.core.modules;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开场白
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/8
 **/
@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(){
        return "Hello World!";
    }

    @GetMapping("/index")
    public String index(){
        return ":: Spring Boot :: (v1.5.10.RELEASE)";
    }

    @GetMapping("/home")
    public String home(){
        return "Welcome to Bamboo background management system!";
    }

    @GetMapping("/unauthorizedurl")
    public String unauthorizedurl(){
        return "No login authentication.401";
    }
}
