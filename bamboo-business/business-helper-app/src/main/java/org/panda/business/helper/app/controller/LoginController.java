package org.panda.business.helper.app.controller;

import io.swagger.annotations.Api;
import org.panda.business.helper.app.model.params.AppLoginParam;
import org.panda.business.helper.app.service.AppUserService;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 系统登录接口
 *
 * @author fangen
 * @since 2020/5/13
 **/
@Api(tags = "系统用户登录")
@RestController
public class LoginController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/login")
    @WebOperationLog(content = "/login", actionType = ActionType.LOGIN, intoStorage = true)
    public RestfulResult<?> login(@RequestBody @Valid AppLoginParam appLoginParam){
        return appUserService.appLogin(appLoginParam);
    }

    @GetMapping("/doLogin")
    public RestfulResult<?> doLogin(HttpServletRequest request){
        return appUserService.loginVerify(request);
    }

    @GetMapping("/logout")
    @WebOperationLog(content = "/logout", actionType = ActionType.QUIT, intoStorage = true)
    public RestfulResult<?> logout(HttpServletRequest request) {
        return appUserService.appLogout(request);
    }
}