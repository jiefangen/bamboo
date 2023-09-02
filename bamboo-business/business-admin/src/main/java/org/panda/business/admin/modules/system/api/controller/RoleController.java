package org.panda.business.admin.modules.system.api.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.common.constant.Authority;
import org.panda.business.admin.modules.system.service.SysRoleService;
import org.panda.business.admin.modules.system.service.dto.SysRoleDto;
import org.panda.business.admin.modules.system.service.entity.SysRole;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAuthorities;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色权限管理
 *
 * @author fangen
 */
@Api(tags = "系统角色管理")
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private SysRoleService roleService;

    @GetMapping("/getRoles")
    @ConfigPermission
    public RestfulResult getRoles() {
        List<SysRoleDto> roles = roleService.getRoles();
        return RestfulResult.success(roles);
    }

    @PostMapping("/add")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_role_add")
    })
    @WebOperationLog(actionType = ActionType.ADD, intoStorage = true)
    public RestfulResult add(@RequestBody SysRole role) {
        String result = roleService.addRole(role);
        if (Commons.RESULT_SUCCESS.equals(result)) {
            return RestfulResult.success();
        }
        return RestfulResult.failure(result);
    }

    @PutMapping("/updateRoleMenu/{roleId}")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_role_update")
    })
    @WebOperationLog(actionType = ActionType.AUTH, intoStorage = true)
    public RestfulResult updateRoleMenu(@PathVariable Integer roleId, @RequestBody SysRoleDto roleDTO) {
        roleService.updateRoleMenu(roleId, roleDTO);
        return RestfulResult.success();
    }

    @DeleteMapping("/del/{roleId}")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_role_del")
    })
    @WebOperationLog(actionType = ActionType.DEL, intoStorage = true)
    public RestfulResult del(@PathVariable Integer roleId) {
        try {
            int result = roleService.deleteRole(roleId);
            if (result < 1) {
                return RestfulResult.failure();
            }
        }catch (BusinessException e){
            return RestfulResult.failure(e.getMessage());
        }
        return RestfulResult.success();
    }
}
