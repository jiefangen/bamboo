package org.panda.business.admin.v1.modules.system.api.controller;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.v1.common.constant.AuthConstants;
import org.panda.business.admin.v1.modules.system.api.vo.MenuVO;
import org.panda.business.admin.v1.modules.system.service.SysMenuService;
import org.panda.business.admin.v1.modules.system.service.SysRoleService;
import org.panda.business.admin.v1.modules.system.service.dto.SysRoleDto;
import org.panda.business.admin.v1.modules.system.service.entity.SysRole;
import org.panda.tech.core.web.restful.RestfulResult;
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
    @Autowired
    private SysMenuService menuService;

    @GetMapping("/getRoutes")
    public RestfulResult getRoutes() {
        List<MenuVO> routes = menuService.getRoutes();
        return RestfulResult.success(routes);
    }

    @GetMapping("/getRoles")
    public RestfulResult getRoles() {
        List<SysRoleDto> roles = roleService.getRoles();
        return RestfulResult.success(roles);
    }

    @PostMapping("/add")
    public RestfulResult add(@RequestBody SysRole role) {
        String roleName = role.getRoleName();
        if(StringUtils.isEmpty(roleName)) {
            return RestfulResult.failure(AuthConstants.PARAMETERS_INCOMPLETE);
        }
        String result = roleService.addRole(role);
        if (!Commons.RESULT_SUCCESS.equals(result)) {
            return RestfulResult.failure(result);
        }
        return RestfulResult.success();
    }

    @PutMapping("/update/{roleId}")
    public RestfulResult update(@PathVariable Integer roleId, @RequestBody SysRoleDto roleDTO) {
        roleService.updateRole(roleId, roleDTO);
        return RestfulResult.success();
    }

    @DeleteMapping("/del/{roleId}")
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
