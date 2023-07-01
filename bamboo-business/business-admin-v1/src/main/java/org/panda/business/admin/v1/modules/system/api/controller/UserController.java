package org.panda.business.admin.v1.modules.system.api.controller;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.v1.common.constant.Authority;
import org.panda.business.admin.v1.common.constant.SystemConstants;
import org.panda.business.admin.v1.modules.system.api.param.ResetPassParam;
import org.panda.business.admin.v1.modules.system.api.param.UserQueryParam;
import org.panda.business.admin.v1.modules.system.api.vo.UserVO;
import org.panda.business.admin.v1.modules.system.service.SysUserService;
import org.panda.business.admin.v1.modules.system.service.dto.SysUserDto;
import org.panda.business.admin.v1.modules.system.service.entity.SysUser;
import org.panda.tech.core.spec.enums.ActionType;
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
    @WebOperationLog(actionType = ActionType.QUERY, intoStorage = true)
    public RestfulResult info(HttpServletRequest request){
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        UserVO userInfo = userService.getUserByToken(token);
        if (userInfo == null) {
            return RestfulResult.failure(SystemConstants.USER_NOT_EXIST_CODE, SystemConstants.USERNAME_NOT_EXIST);
        }
        return RestfulResult.success(userInfo);
    }

    @PostMapping("/page")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(permission = Authority.ROLE_USER),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_user_page")
    })
    @WebOperationLog(actionType = ActionType.QUERY, intoStorage = true)
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
    public RestfulResult add(@RequestBody SysUser user){
        if(StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return RestfulResult.failure(SystemConstants.PARAMETERS_INCOMPLETE);
        }
        boolean result = userService.addUser(user);
        if (result) {
            return RestfulResult.failure();
        }
        return RestfulResult.success();
    }

    @PutMapping("/edit")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(permission = Authority.ROLE_USER),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_user_edit")
    })
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult edit(@RequestBody SysUser user){
        // 重置参数
        user.setPassword(null);
        boolean result = userService.updateUser(user);
        if (result) {
            return RestfulResult.failure();
        }
        return RestfulResult.success();
    }

    @PostMapping("/updatePassword")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(permission = Authority.ROLE_USER),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_user_updatePassword")
    })
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult resetPassword(@RequestBody ResetPassParam resetPassParam){
        String username = resetPassParam.getUsername();
        String oldPassword = resetPassParam.getOldPassword();
        String newPassword = resetPassParam.getNewPassword();
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            return RestfulResult.failure(SystemConstants.PARAMETERS_INCOMPLETE);
        }
        String result = userService.resetPassword(username, oldPassword, newPassword);
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
    public RestfulResult del(@PathVariable String username){
        try {
            boolean result = userService.deleteUser(username);
            if (result) {
                return RestfulResult.failure();
            }
        }catch (BusinessException e){
            return RestfulResult.failure(e.getMessage());
        }
        return RestfulResult.success();
    }

    @PostMapping("/updateUserRole")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(permission = Authority.ROLE_USER),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_user_updateUserRole")
    })
    @WebOperationLog(actionType = ActionType.AUTH, intoStorage = true)
    public RestfulResult updateUserRole(@RequestBody SysUserDto userDto){
        userService.updateUserRole(userDto);
        return RestfulResult.success();
    }

}