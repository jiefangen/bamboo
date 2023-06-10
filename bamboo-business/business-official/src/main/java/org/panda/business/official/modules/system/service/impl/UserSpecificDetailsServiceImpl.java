package org.panda.business.official.modules.system.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.official.common.constant.UserAuthConstants;
import org.panda.business.official.modules.system.service.ISysUserRoleService;
import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.business.official.modules.system.service.entity.SysUser;
import org.panda.tech.core.spec.user.DefaultUserIdentity;
import org.panda.tech.security.config.exception.BusinessAuthenticationException;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.panda.tech.security.user.UserGrantedAuthority;
import org.panda.tech.security.user.UserSpecificDetails;
import org.panda.tech.security.user.UserSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户特性细节服务组件
 *
 * @author fangen
 **/
@Component
public class UserSpecificDetailsServiceImpl implements UserSpecificDetailsService {

    @Autowired
    private ISysUserRoleService userRoleService;

    private PasswordEncoder passwordEncoder;

    @Override
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserSpecificDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new BusinessAuthenticationException(UserAuthConstants.USERNAME_NOT_EXIST);
        }
        // 用户信息查询判断拦截
        SysUserDto sysUserDto = userRoleService.getUserAndRoles(username);
        if (sysUserDto == null || sysUserDto.getUser() == null) {
            LogUtil.error(getClass(), UserAuthConstants.USERNAME_NOT_EXIST);
            throw new UsernameNotFoundException(UserAuthConstants.USERNAME_NOT_EXIST);
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
        grantedAuthority.setPermissions(sysUserDto.getRoleCodes());

        authorities.add(grantedAuthority);
        userSpecificDetails.setAuthorities(authorities);
        userSpecificDetails.setEnabled(sysUser.getEnabled());
        return userSpecificDetails;
    }

}
