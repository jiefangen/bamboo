package org.panda.business.admin.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.panda.business.admin.modules.system.api.param.PermissionQueryParam;
import org.panda.business.admin.modules.system.service.SysPermissionService;
import org.panda.business.admin.modules.system.service.SysRolePermissionService;
import org.panda.business.admin.modules.system.service.entity.SysPermission;
import org.panda.business.admin.modules.system.service.entity.SysRolePermission;
import org.panda.business.admin.modules.system.service.repository.SysPermissionMapper;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.util.QueryPageHelper;
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
        List<SysPermission> sysPermissions = new ArrayList<>();
        if (roleId == null) {
            return sysPermissions;
        }
        LambdaQueryWrapper<SysRolePermission> rolePermissionWrapper = new LambdaQueryWrapper<>();
        rolePermissionWrapper.eq(SysRolePermission::getRoleId, roleId);
        List<SysRolePermission> rolePermissions = rolePermissionService.list(rolePermissionWrapper);
        if (!rolePermissions.isEmpty()) {
            List<Integer> permissionIds = rolePermissions.stream()
                    .map(rolePermission -> rolePermission.getPermissionId())
                    .collect(Collectors.toList());
            sysPermissions = this.listByIds(permissionIds);
        }
        return sysPermissions;
    }

    @Override
    public QueryResult<SysPermission> getPermissionsByPage(PermissionQueryParam queryParam) {
        Page<SysPermission> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryParam.getKeyword())) {
            queryWrapper.like(SysPermission::getPermissionName, queryParam.getKeyword())
                    .like(SysPermission::getOperationScope, queryParam.getKeyword());
        }
        queryWrapper.orderByAsc(SysPermission::getId);
        IPage<SysPermission> actionLogPage = this.page(page, queryWrapper);
        QueryResult<SysPermission> queryResult = QueryPageHelper.convertQueryResult(actionLogPage);
        return queryResult;
    }
}
