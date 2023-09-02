package org.panda.business.admin.modules.system.api.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.common.constant.Authority;
import org.panda.business.admin.common.constant.SystemConstants;
import org.panda.business.admin.modules.system.api.param.*;
import org.panda.business.admin.modules.system.api.vo.UserVO;
import org.panda.business.admin.modules.system.service.SysUserService;
import org.panda.business.admin.modules.system.service.entity.SysUser;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigAuthorities;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户权限管理
 *
 * @author fangen
 **/
@Api(tags = "系统用户管理")
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private SysUserService userService;

    @GetMapping("/info")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.QUERY, intoStorage = false)
    public RestfulResult info(HttpServletRequest request) {
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        UserVO userInfo = userService.getUserByToken(token);
        if (userInfo == null) {
            return RestfulResult.failure(SystemConstants.USER_NOT_EXIST_CODE, SystemConstants.USERNAME_NOT_EXIST);
        }
        return RestfulResult.success(userInfo);
    }

    @PostMapping("/page")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.QUERY)
    public RestfulResult page(@RequestBody UserQueryParam queryParam) {
        QueryResult<UserVO> userPage = userService.getUserByPage(queryParam);
        return RestfulResult.success(userPage);
    }

    @PostMapping("/add")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(permission = Authority.ROLE_USER),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_user_add")
    })
    @WebOperationLog(actionType = ActionType.ADD, intoStorage = true)
    public RestfulResult add(@RequestBody @Valid AddUserParam userParam) {
        String result = userService.addUser(userParam);
        if (Commons.RESULT_SUCCESS.equals(result)) {
            return RestfulResult.success();
        }
        return RestfulResult.failure(result);
    }

    @PutMapping("/edit")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(permission = Authority.ROLE_USER),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_user_edit")
    })
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult edit(@RequestBody SysUser user) {
        boolean result = userService.updateUser(user);
        if (result) {
            return RestfulResult.success();
        }
        return RestfulResult.failure();
    }

    @PutMapping("/updateAccount")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult updateAccount(@RequestBody @Valid UpdateAccountParam updateAccountParam) {
        boolean result = userService.updateAccount(updateAccountParam);
        if (result) {
            return RestfulResult.success();
        }
        return RestfulResult.failure();
    }

    @PostMapping("/updatePassword")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult updatePassword(@RequestBody @Valid UpdatePassParam updatePassParam) {
        String result = userService.updatePassword(updatePassParam);
        if (Commons.RESULT_SUCCESS.equals(result)) {
            return RestfulResult.success();
        } else {
            return RestfulResult.failure(result);
        }
    }

    @PostMapping("/resetPassword")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(permission = Authority.ROLE_USER),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_user_resetPassword")
    })
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult resetPassword(@RequestBody @Valid ResetPassParam resetPassParam) {
        String result = userService.resetPassword(resetPassParam);
        if (Commons.RESULT_SUCCESS.equals(result)) {
            return RestfulResult.success();
        } else {
            return RestfulResult.failure(result);
        }
    }

    @DeleteMapping("/del/{username}")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(permission = Authority.ROLE_USER),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_user_del")
    })
    @WebOperationLog(actionType = ActionType.DEL, intoStorage = true)
    public RestfulResult del(@PathVariable String username) {
        try {
            boolean result = userService.deleteUser(username);
            if (result) {
                return RestfulResult.success();
            }
        } catch (BusinessException e){
            return RestfulResult.failure(e.getMessage());
        }
        return RestfulResult.failure();
    }

    @PostMapping("/updateUserRole")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(permission = Authority.ROLE_USER),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_user_updateUserRole")
    })
    @WebOperationLog(actionType = ActionType.AUTH, intoStorage = true)
    public RestfulResult updateUserRole(@RequestBody @Valid UpdateUserRoleParam userRoleParam) {
        userService.updateUserRole(userRoleParam);
        return RestfulResult.success();
    }

}
