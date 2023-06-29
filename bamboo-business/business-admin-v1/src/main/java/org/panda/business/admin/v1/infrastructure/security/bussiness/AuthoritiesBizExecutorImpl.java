package org.panda.business.admin.v1.infrastructure.security.bussiness;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.AtomicCounter;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.business.admin.v1.common.constant.enums.RoleCode;
import org.panda.business.admin.v1.modules.system.service.SysPermissionService;
import org.panda.business.admin.v1.modules.system.service.SysRolePermissionService;
import org.panda.business.admin.v1.modules.system.service.SysRoleService;
import org.panda.business.admin.v1.modules.system.service.entity.SysPermission;
import org.panda.business.admin.v1.modules.system.service.entity.SysRole;
import org.panda.business.admin.v1.modules.system.service.entity.SysRolePermission;
import org.panda.tech.security.user.UserConfigAuthority;
import org.panda.tech.security.web.AuthoritiesBizExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限集业务扩展实现
 *
 * @author fangen
 **/
@Component
public class AuthoritiesBizExecutorImpl implements AuthoritiesBizExecutor {
    /**
     * 映射集：api路径-所需权限清单
     */
    private final Map<String, Collection<UserConfigAuthority>> apiConfigAuthoritiesMapping = new HashMap<>();
    public static final String SOURCE_EVENT = "applicationEvent";
    public static final String ASSOC_LOAD = "autoload";

    private AtomicCounter permissionCounter = new AtomicCounter();
    private AtomicCounter rolePerCounter = new AtomicCounter();

    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private SysRolePermissionService rolePermissionService;

    @Override
    @Transactional
    public void execute() {
        if (this.apiConfigAuthoritiesMapping.isEmpty()) {
            return;
        }
        // 重置系统自动生成的权限集
        this.resetPermissions();
        for (Map.Entry<String, Collection<UserConfigAuthority>> authorityEntry : this.apiConfigAuthoritiesMapping.entrySet()) {
            Collection<UserConfigAuthority> userConfigAuthorities = authorityEntry.getValue();
            // 系统权限限定标识集
            List<String> systemPermissionList = userConfigAuthorities.stream()
                    .filter(userConfigAuthority -> !RoleCode.isSystemRole(userConfigAuthority.getPermission()))
                    .map(userConfigAuthority -> userConfigAuthority.getPermission())
                    .collect(Collectors.toList());
            List<String> systemRolePerList = userConfigAuthorities.stream()
                    .filter(userConfigAuthority -> RoleCode.isSystemRole(userConfigAuthority.getPermission()))
                    .map(userConfigAuthority -> userConfigAuthority.getPermission())
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(systemPermissionList)) {
                String apiUrl = authorityEntry.getKey();
                if (systemRolePerList.isEmpty()) { // 系统角色未配置即所有角色都拥有此权限
                    List<SysRole> roles = roleService.list();
                    List<Integer> roleIds = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
                    this.savePerRelationship(systemPermissionList, roleIds, apiUrl);
                } else { // 指定角色下的权限限定
                    LambdaQueryWrapper<SysRole> roleQueryWrapper = new LambdaQueryWrapper<>();
                    // admin角色默认拥有所有权限
                    systemRolePerList.add(RoleCode.ADMIN.name());
                    roleQueryWrapper.in(SysRole::getRoleCode, systemRolePerList);
                    List<SysRole> roles = roleService.list(roleQueryWrapper);
                    List<Integer> roleIds = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
                    this.savePerRelationship(systemPermissionList, roleIds, apiUrl);
                }
            }
        }
        LogUtil.info(getClass(), "System permission limit loading complete");
    }

    /**
     * 保存系统权限关系
     */
    private void savePerRelationship(List<String> systemPermissionList, List<Integer> roleIds, String operationScope) {
        systemPermissionList.forEach(systemPermission -> {
            SysPermission permission = new SysPermission();
            permission.setId(permissionCounter.getNextCount());
            permission.setPermissionName(systemPermission);
            permission.setPermissionCode(systemPermission.toUpperCase());
            permission.setDescription("Application initialization loading");
            permission.setResourcesId(JsonUtil.toJson(roleIds));
            permission.setResourcesType("api");
            permission.setSource(SOURCE_EVENT);
            permission.setOperationScope(operationScope);
            boolean retBool = permissionService.save(permission);
            if (retBool) {
                LambdaQueryWrapper<SysPermission> perQueryWrapper = new LambdaQueryWrapper<>();
                perQueryWrapper.eq(SysPermission::getPermissionName, systemPermission);
                perQueryWrapper.eq(SysPermission::getPermissionCode, systemPermission.toUpperCase());
                SysPermission sysPermission = permissionService.getOne(perQueryWrapper);
                Integer permissionId = sysPermission.getId();
                // 更新角色权限关系表
                List<SysRolePermission> rolePermissions = new ArrayList<>();
                roleIds.forEach(roleId -> {
                    SysRolePermission rolePermission = new SysRolePermission();
                    rolePermission.setId(rolePerCounter.getNextCount());
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(permissionId);
                    rolePermission.setAssociation(ASSOC_LOAD);
                    rolePermissions.add(rolePermission);
                });
                rolePermissionService.saveBatch(rolePermissions);
            }
        });
    }

    /**
     * 重置系统自动生成的权限集
     */
    private void resetPermissions() {
        LambdaQueryWrapper<SysRolePermission> rolePermissionWrapper = new LambdaQueryWrapper<>();
        rolePermissionWrapper.eq(SysRolePermission::getAssociation, ASSOC_LOAD);
        rolePermissionWrapper.between(SysRolePermission::getId, 1, 999);
        rolePermissionService.remove(rolePermissionWrapper);
        LambdaQueryWrapper<SysPermission> permissionWrapper = new LambdaQueryWrapper<>();
        permissionWrapper.eq(SysPermission::getSource, SOURCE_EVENT);
        permissionWrapper.between(SysPermission::getId, 1, 999);
        permissionService.remove(permissionWrapper);
    }

    @Override
    public void setApiConfigAuthoritiesMapping(String api, Collection<UserConfigAuthority> authorities) {
        if (StringUtils.isNotBlank(api)) {
            this.apiConfigAuthoritiesMapping.put(api, authorities);
        }
    }

    @Override
    public String[] getUrlPatterns() {
        return new String[]{"/system/**"};
    }
}
