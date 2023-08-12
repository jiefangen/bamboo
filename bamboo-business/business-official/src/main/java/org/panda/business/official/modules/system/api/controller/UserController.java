package org.panda.business.official.modules.system.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.official.modules.system.service.ISysUserRoleService;
import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
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
    private ISysUserRoleService userRoleService;

    /**
     * 获取用户详情
     */
    @GetMapping("/getUserDetails")
    @ConfigAnonymous
    public RestfulResult getUserDetails(@RequestParam String username) {
        SysUserDto sysUserDto = userRoleService.getUserAndRoles(username);
        return RestfulResult.success(sysUserDto);
    }

}
