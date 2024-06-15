package org.panda.business.helper.app.controller;

import io.swagger.annotations.Api;
import org.panda.business.helper.app.common.constant.ProjectConstants;
import org.panda.business.helper.app.model.vo.UserInfo;
import org.panda.business.helper.app.service.AppUserService;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    @WebOperationLog(actionType = ActionType.QUERY, intoStorage = true)
    public RestfulResult<?> info(HttpServletRequest request){
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        UserInfo userInfo = appUserService.getUserByToken(token);
        if (userInfo == null) {
            return RestfulResult.failure(ProjectConstants.USER_NOT_EXIST_CODE, ProjectConstants.USERNAME_NOT_EXIST);
        }
        return RestfulResult.success(userInfo);
    }

}
