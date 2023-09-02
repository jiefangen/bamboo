package org.panda.business.admin.modules.system.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.modules.system.api.param.PermissionQueryParam;
import org.panda.business.admin.modules.system.service.SysPermissionService;
import org.panda.business.admin.modules.system.service.entity.SysPermission;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单权限管理
 *
 * @author fangen
 */
@Api(tags = "系统权限管理")
@RestController
@RequestMapping("/system/permission")
public class PermissionController {

    @Autowired
    private SysPermissionService permissionService;

    @PostMapping("/page")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.QUERY)
    public RestfulResult page(@RequestBody PermissionQueryParam queryParam) {
        QueryResult<SysPermission> permissionPage = permissionService.getPermissionsByPage(queryParam);
        return RestfulResult.success(permissionPage);
    }

    @GetMapping("/getPermission")
    @ConfigPermission
    public RestfulResult getPermission(Integer roleId){
        List<SysPermission> permissionList = permissionService.getPermissions(roleId);
        return RestfulResult.success(permissionList);
    }
}
