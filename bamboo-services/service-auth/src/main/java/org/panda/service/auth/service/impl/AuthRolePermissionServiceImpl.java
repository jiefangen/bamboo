package org.panda.service.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.service.auth.infrastructure.security.app.AppServiceModel;
import org.panda.service.auth.infrastructure.security.app.authority.AppConfigAuthority;
import org.panda.service.auth.infrastructure.security.app.enums.AuthRoleCode;
import org.panda.service.auth.model.entity.AuthPermission;
import org.panda.service.auth.model.entity.AuthRole;
import org.panda.service.auth.model.entity.AuthRolePermission;
import org.panda.service.auth.repository.AuthRolePermissionMapper;
import org.panda.service.auth.service.AuthPermissionService;
import org.panda.service.auth.service.AuthRolePermissionService;
import org.panda.service.auth.service.AuthRoleService;
import org.panda.tech.core.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色权限关系 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-11-04
 */
@Service
public class AuthRolePermissionServiceImpl extends ServiceImpl<AuthRolePermissionMapper, AuthRolePermission> implements AuthRolePermissionService {

    @Autowired
    private AuthRoleService authRoleService;
    @Autowired
    private AuthPermissionService authPermissionService;

    @Override
    public void initPermissions(List<AppServiceModel.Permission> permissions, Integer appServerId, String appName) {
        if (CollectionUtils.isEmpty(permissions)) {
            return;
        }
        this.savePermissions(permissions, appServerId, appName);
    }

    private void savePermissions(List<AppServiceModel.Permission> permissions, Integer appServerId, String appName) {
        permissions.forEach(permission -> {
            String resources = permission.getApi();
            StringBuilder permissionName = new StringBuilder(CommonUtil.getDefaultPermission(resources));
            int braceCount = CommonUtil.countPairsOfBraces(resources);
            if (braceCount > 0) {
                for (int i = 0; i < braceCount; i++) {
                    permissionName.append(Strings.UNDERLINE + Strings.ASTERISK);
                }
            }
            String permissionCode = permissionName.toString().toUpperCase();
            LambdaQueryWrapper<AuthPermission> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AuthPermission::getPermissionCode, permissionCode);
            queryWrapper.eq(AuthPermission::getSource, appName);
            queryWrapper.eq(AuthPermission::getResources, resources);
            AuthPermission authPermission = authPermissionService.getOne(queryWrapper);

            if (authPermission == null) {
                AuthPermission permissionParam = new AuthPermission();
                permissionParam.setPermissionName(permissionName.toString());
                permissionParam.setPermissionCode(permissionCode);
                permissionParam.setResourcesId(appServerId);
                permissionParam.setResourcesType(permission.getResourcesType());
                permissionParam.setSource(appName);
                permissionParam.setResources(resources);
                List<AppConfigAuthority> configAuthorityList = (List<AppConfigAuthority>) permission.getAppConfigAuthorities();
                if (CollectionUtils.isNotEmpty(configAuthorityList)) {
                    // 默认使用权限集中的首个权限来限定
                    AppConfigAuthority configAuthority = configAuthorityList.get(0);
                    permissionParam.setScope(configAuthority.toString());
                    permissionParam.setDescription(configAuthority.getMode());
                }
                boolean retBool = authPermissionService.save(permissionParam);
                if (retBool) {
                    authPermission = authPermissionService.getOne(queryWrapper);
                }
            }
            if (authPermission != null) {
                this.saveAuthPerRelationship(authPermission.getId(), permission.getAppConfigAuthorities());
            }
        });
    }

    private void saveAuthPerRelationship(Integer permissionId, Collection<AppConfigAuthority> configAuthorities) {
        if (CollectionUtils.isNotEmpty(configAuthorities)) {
            configAuthorities.forEach(configAuthority -> {
                String rolePermission = configAuthority.getPermission();
                List<String> rolePermissions = new ArrayList<>();
                if (StringUtils.isNotEmpty(rolePermission)) {
                    if (rolePermission.contains(Strings.COMMA)) {
                        rolePermissions.addAll(Arrays.asList(rolePermission.split(Strings.COMMA)));
                    } else {
                        rolePermissions.add(rolePermission);
                    }
                }
                // 加入顶级角色权限，默认顶级角色拥有全部资源权限
                rolePermissions.addAll(AuthRoleCode.getTopRoles());
                for (String roleCode : rolePermissions) {
                    LambdaQueryWrapper<AuthRole> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(AuthRole::getRoleCode, roleCode);
                    AuthRole authRole = authRoleService.getOne(queryWrapper, false);
                    if (authRole != null) {
                        LambdaQueryWrapper<AuthRolePermission> rolePermissionQuery = new LambdaQueryWrapper<>();
                        rolePermissionQuery.eq(AuthRolePermission::getPermissionId, permissionId);
                        rolePermissionQuery.eq(AuthRolePermission::getRoleId, authRole.getId());
                        if (this.count(rolePermissionQuery) < 1) {
                            AuthRolePermission authRolePermission = new AuthRolePermission();
                            authRolePermission.setPermissionId(permissionId);
                            authRolePermission.setRoleId(authRole.getId());
                            this.save(authRolePermission);
                        }
                    }
                }
            });
        }
    }

}
