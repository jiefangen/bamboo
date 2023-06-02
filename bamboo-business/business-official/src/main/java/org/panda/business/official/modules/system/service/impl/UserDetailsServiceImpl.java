package org.panda.business.official.modules.system.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.panda.business.official.common.constant.UserAuthConstants;
import org.panda.tech.security.config.exception.BusinessAuthenticationException;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.panda.tech.security.user.UserSpecificDetails;
import org.panda.tech.security.user.UserSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class UserDetailsServiceImpl implements UserSpecificDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserSpecificDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new BusinessAuthenticationException(UserAuthConstants.USERNAME_NOT_EXIST);
        }
        // TODO 用户信息查询判断拦截
        Object user = new Object();
        if (user == null) {
            throw new UsernameNotFoundException(UserAuthConstants.USERNAME_NOT_EXIST);
        }

        // 组装用户特性细节
        DefaultUserSpecificDetails userSpecificDetails = new DefaultUserSpecificDetails();
        userSpecificDetails.setUsername(username);
        String encodedPassword = passwordEncoder.encode("123456");
        userSpecificDetails.setPassword(encodedPassword);

        // 添加角色鉴权以及权限鉴权，每次访问带有权限限制的接口时就会验证，拥有对应权限code的话才可以正常访问。
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 角色必须以`ROLE_`开头，数据库中没有，则在这里加
        authorities.add(new SimpleGrantedAuthority("ROLE_" + "ADMIN"));

        userSpecificDetails.setAuthorities(authorities);
        return userSpecificDetails;
    }

}
