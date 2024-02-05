package org.panda.service.auth.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证服务登录控制器
 *
 * @author fangen
 **/
@Api(tags = "认证服务登录控制器")
@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password, @RequestParam String service) {
        // 用于安全认证登录引导，无需处理任何逻辑
        // 登录请求被过滤器拦截处理，本方法实际上不会被调用
    }

}
