package org.panda.business.example.modules;

import io.swagger.annotations.Api;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "样例平台登录控制器")
@RestController
public class LoginController {

    @PostMapping("/login")
    public RestfulResult<?> login(@RequestParam String username, @RequestParam String password,
                                  @RequestParam(required = false) String loginMode) {
        // 用于安全认证登录引导，无需处理任何逻辑
        return RestfulResult.success(username + password + loginMode);
    }

    @GetMapping("/logout")
    public RestfulResult<?> logout() {
        return RestfulResult.success();
    }
}
