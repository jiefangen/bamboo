package org.panda.business.helper.app.controller;

import io.swagger.annotations.Api;
import org.panda.business.helper.app.model.params.AppLoginParam;
import org.panda.business.helper.app.service.AppUserService;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.spec.log.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * APP登录接口
 *
 * @author fangen
 * @since 2020/5/13
 **/
@Api(tags = "APP用户登录")
@RestController
@RequestMapping("/app")
public class LoginController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/login")
    @WebOperationLog(content = "/app/login", actionType = ActionType.LOGIN, intoStorage = true)
    public RestfulResult<?> login(@RequestBody @Valid AppLoginParam appLoginParam, HttpServletRequest request) {
        return appUserService.appLogin(appLoginParam, request);
    }

    @PostMapping("/auth/login")
    @WebOperationLog(actionType = ActionType.LOGIN, intoStorage = true)
    public RestfulResult<?> authLogin(@RequestBody @Valid AppLoginParam appLoginParam, HttpServletRequest request) {
        return appUserService.authAppLogin(appLoginParam, request);
    }

    @GetMapping("/doLogin")
    public RestfulResult<?> doLogin(HttpServletRequest request) {
        return appUserService.loginVerify(request);
    }

    @GetMapping("/logout")
    @WebOperationLog(actionType = ActionType.QUIT, intoStorage = true)
    public RestfulResult<?> logout(HttpServletRequest request) {
        return appUserService.appLogout(request);
    }
}
