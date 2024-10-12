package org.panda.business.official.modules.system.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.official.modules.system.service.SysUserRoleService;
import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 官网平台用户控制器
 *
 * @author fangen
 **/
@Api(tags = "官网平台用户控制器")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserRoleService userRoleService;

    /**
     * 获取用户详情
     */
    @GetMapping("/getUserDetails")
    public RestfulResult<?> getUserDetails(@RequestParam String username) {
        SysUserDto sysUserDto = userRoleService.getUserAndRoles(username);
        if (sysUserDto == null) {
            return RestfulResult.failure();
        }
        return RestfulResult.success(sysUserDto);
    }
}
