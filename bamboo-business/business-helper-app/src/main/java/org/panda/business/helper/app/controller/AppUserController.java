package org.panda.business.helper.app.controller;

import io.swagger.annotations.Api;
import org.panda.business.helper.app.model.params.UpdateUserParam;
import org.panda.business.helper.app.model.vo.UserInfo;
import org.panda.business.helper.app.service.AppUserService;
import org.panda.tech.shiro.annotation.Accessibility;
import org.panda.tech.shiro.authority.PerConstants;
import org.panda.tech.core.exception.business.auth.AuthConstants;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.spec.log.annotation.WebOperationLog;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.bamboo.common.model.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * APP用户接口
 *
 * @author fangen
 * @since 2024/6/13
 **/
@Api(tags = "APP用户详情")
@RestController
@RequestMapping("/app/user")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @GetMapping("/info")
    @WebOperationLog(actionType = ActionType.QUERY)
    @Accessibility(role = PerConstants.ROLE_ACCOUNT)
    public RestfulResult<?> info(HttpServletRequest request) {
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        UserInfo userInfo = appUserService.getUserByToken(token);
        if (userInfo == null) {
            return RestfulResult.failure(AuthConstants.USER_NOT_EXIST_CODE, AuthConstants.USERNAME_NOT_EXIST);
        }
        return RestfulResult.success(userInfo);
    }

    @PostMapping("/update")
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult<?> update(@RequestBody @Valid UpdateUserParam updateUserParam, HttpServletRequest request) {
        if (appUserService.updateUser(updateUserParam, request)) {
            return RestfulResult.success();
        }
        return RestfulResult.failure();
    }

}
