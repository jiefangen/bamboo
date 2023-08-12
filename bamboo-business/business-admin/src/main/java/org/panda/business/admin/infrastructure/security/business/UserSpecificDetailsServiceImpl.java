package org.panda.business.admin.infrastructure.security.business;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.exception.business.param.RequiredParamException;
import org.panda.business.admin.common.constant.SystemConstants;
import org.panda.business.admin.modules.system.service.SysPermissionService;
import org.panda.business.admin.modules.system.service.SysUserService;
import org.panda.business.admin.modules.system.service.dto.SysUserDto;
import org.panda.business.admin.modules.system.service.entity.SysPermission;
import org.panda.business.admin.modules.system.service.entity.SysRole;
import org.panda.business.admin.modules.system.service.entity.SysUser;
import org.panda.tech.core.spec.user.DefaultUserIdentity;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.panda.tech.security.user.UserGrantedAuthority;
import org.panda.tech.security.user.UserSpecificDetails;
import org.panda.tech.security.user.UserSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户特性细节服务组件
 *
 * @author fangen
 **/
@Component
public class UserSpecificDetailsServiceImpl implements UserSpecificDetailsService {

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserSpecificDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new RequiredParamException();
        }
        // 用户信息查询判断拦截
        SysUserDto sysUserDto = userService.getUserAndRoles(username);
        if (sysUserDto == null || sysUserDto.getUser() == null) {
            throw new UsernameNotFoundException(SystemConstants.USERNAME_NOT_EXIST);
        }
        SysUser sysUser = sysUserDto.getUser();
        // 组装用户特性细节
        DefaultUserSpecificDetails userSpecificDetails = new DefaultUserSpecificDetails();
        userSpecificDetails.setUsername(username);
        userSpecificDetails.setCaption(sysUser.getNickname());
        // 从数据库中直接取加密密码
        String encodedPassword = sysUser.getPassword();
        userSpecificDetails.setPassword(encodedPassword);
        userSpecificDetails.setIdentity(new DefaultUserIdentity(sysUser.getUserType(), sysUser.getId()));

        // 添加角色鉴权以及权限鉴权，每次访问带有权限限制的接口时就会验证，拥有对应权限code的话才可以正常访问。
        List<GrantedAuthority> authorities = new ArrayList<>();
        UserGrantedAuthority grantedAuthority = new UserGrantedAuthority();
        grantedAuthority.setType(sysUser.getUserType());
        grantedAuthority.setRank(sysUser.getUserRank());
        grantedAuthority.setApp(Strings.ASTERISK); // 通配符'*'
        grantedAuthority.setPermissions(this.buildPermissions(sysUserDto.getRoles()));
        authorities.add(grantedAuthority);
        userSpecificDetails.setAuthorities(authorities);

        // 账户状态配置
        userSpecificDetails.setEnabled(sysUser.getEnabled());
        userSpecificDetails.setAccountNonLocked(true);
        userSpecificDetails.setAccountNonExpired(true);
        userSpecificDetails.setCredentialsNonExpired(true);
        return userSpecificDetails;
    }

    /**
     * 通过角色集构建权限限定集
     *
     * @param roles 角色集
     * @return 权限限定集
     */
    private Set<String> buildPermissions(List<SysRole> roles) {
        Set<String> permissions = new HashSet<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            List<SysPermission> sysPermissions = new ArrayList<>();
            roles.forEach(role -> {
                List<SysPermission> permission = permissionService.getPermissions(role.getId());
                sysPermissions.addAll(permission);
            });
            permissions = sysPermissions.stream().map(permission -> permission.getPermissionCode()).collect(Collectors.toSet());
        }
        return permissions;
    }
}
