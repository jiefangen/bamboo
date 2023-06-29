package org.panda.business.admin.v1.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.panda.business.admin.v1.modules.system.service.SysRolePermissionService;
import org.panda.business.admin.v1.modules.system.service.entity.SysPermission;
import org.panda.business.admin.v1.modules.system.service.entity.SysRolePermission;
import org.panda.business.admin.v1.modules.system.service.repository.SysPermissionMapper;
import org.panda.business.admin.v1.modules.system.service.SysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统权限 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-25
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Autowired
    private SysRolePermissionService rolePermissionService;

    @Override
    public List<SysPermission> getPermissions(Integer roleId) {
        LambdaQueryWrapper<SysRolePermission> rolePermissionWrapper = new LambdaQueryWrapper<>();
        rolePermissionWrapper.eq(SysRolePermission::getRoleId, roleId);
        List<SysRolePermission> rolePermissions = rolePermissionService.list(rolePermissionWrapper);
        List<SysPermission> sysPermissions = new ArrayList<>();
        if (!rolePermissions.isEmpty()) {
            List<Integer> permissionIds = rolePermissions.stream()
                    .map(rolePermission -> rolePermission.getPermissionId())
                    .collect(Collectors.toList());
            sysPermissions = this.listByIds(permissionIds);
        }
        return sysPermissions;
    }
}
