package org.panda.business.admin.modules;

import io.swagger.annotations.Api;
import org.panda.business.admin.infrastructure.security.authentication.LogoutAuthenticationHandler;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.bamboo.common.model.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台管理系统登录控制器
 *
 * @author fangen
 * @since 2023-05-27
 **/
@Api(tags = "系统登录控制器")
@RestController
public class LoginController {

    @Autowired
    private LogoutAuthenticationHandler logoutAuthenticationHandler;

    @PostMapping("/login")
    public RestfulResult<?> login(@RequestParam String username, @RequestParam String password,
                                  @RequestParam(required = false) String loginMode) {
        // 用于安全认证登录引导，无需处理任何逻辑
        return RestfulResult.success(username + password + loginMode);
    }

    @GetMapping("/logout")
    public RestfulResult<?> logout() {
        HttpServletRequest request = SpringWebContext.getRequest();
        if (request != null) {
            logoutAuthenticationHandler.onLogout(request);
        }
        return RestfulResult.success();
    }
}
