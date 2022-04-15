package org.panda.modules;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * opener
 *
 * @author fangen
 * @since JDK 11  2020/5/8
 **/
@Api(tags = "开场白")
@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(){
        return "Hello World!";
    }

    @GetMapping("/home")
    public String home(){
        return "Welcome to Bamboo background management system!";
    }

    @GetMapping("/unauthorizedUrl")
    public String unauthorizedUrl(){
        return "No login authentication 403";
    }

    @GetMapping("/index/{param}")
    public String index(@PathVariable String param){
        return param + " :: Spring Boot :: (v2.6.6)";
    }
}
