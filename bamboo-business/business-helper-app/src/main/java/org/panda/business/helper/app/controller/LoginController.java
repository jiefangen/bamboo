package org.panda.business.helper.app.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.helper.app.common.constant.ProjectConstants;
import org.panda.business.helper.app.common.utils.TokenUtil;
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
    @WebOperationLog(content = "/login", actionType= ActionType.LOGIN, intoStorage = true)
    public RestfulResult<?> login(@RequestBody @Valid AppLoginParam appLoginParam){
        return appUserService.appLogin(appLoginParam);
    }

    @GetMapping("/doLogin")
    public RestfulResult<?> doLogin(HttpServletRequest request){
        String token = request.getHeader(ProjectConstants.AUTH_HEADER);
        if (StringUtils.isNotEmpty(token)) {
            try {
                TokenUtil.verify(token);
            } catch (Exception e) {
                if (e instanceof TokenExpiredException) {
                    LogUtil.warn(getClass(), e.getMessage());
                    return RestfulResult.failure(ProjectConstants.TOKEN_EXPIRED, e.getMessage());
                }
            }
        }
        return RestfulResult.failure(ProjectConstants.LOGGED_OUT, ProjectConstants.LOGGED_OUT_REASON);
    }

    @GetMapping("/logout")
    @WebOperationLog(content = "/logout", actionType= ActionType.QUIT, intoStorage = true)
    public RestfulResult<?> logout(){
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.isAuthenticated()) {
//            subject.logout();
//        }
        return RestfulResult.success();
    }
}
