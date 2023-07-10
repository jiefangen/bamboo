package org.panda.business.admin.v1.modules;

import io.swagger.annotations.Api;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理系统登录控制器
 *
 * @author fangen
 * @since 2023-05-27
 **/
@Api(tags = "系统登录控制器")
@RestController
public class LoginController {

    @PostMapping("/login")
    public RestfulResult login(@RequestParam String username, @RequestParam String password,
                               @RequestParam(required = false) String loginMode) {
        // 用于安全认证登录引导，无需处理任何逻辑
        return RestfulResult.success(username + password + loginMode);
    }

    @PostMapping("/logout")
    public RestfulResult logout() {
        // 用于登出流程引导，无需处理任何逻辑
        return RestfulResult.success();
    }

}
