package org.panda.service.auth.infrastructure.security.authorization;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.service.auth.common.constant.AuthConstants;
import org.panda.service.auth.model.dto.AuthAccountDto;
import org.panda.service.auth.model.entity.AuthAccount;
import org.panda.service.auth.service.AuthAccountService;
import org.panda.tech.core.exception.business.param.RequiredParamException;
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
import java.util.List;

/**
 * 账户特性细节服务组件
 *
 * @author fangen
 **/
@Component
public class AccountSpecificDetailsServiceImpl implements UserSpecificDetailsService {

    @Autowired
    private AuthAccountService accountService;

    @Override
    public UserSpecificDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new RequiredParamException();
        }
        // 账户信息查询判断拦截
        AuthAccountDto authAccountDto = accountService.getAccountAndRoles(username);
        if (authAccountDto == null || authAccountDto.getAccount() == null) {
            throw new UsernameNotFoundException(AuthConstants.ACCOUNT_NOT_EXIST);
        }
        AuthAccount authAccount = authAccountDto.getAccount();
        // 组装账户特性细节
        DefaultUserSpecificDetails userSpecificDetails = new DefaultUserSpecificDetails();
        userSpecificDetails.setUsername(username);
        userSpecificDetails.setCaption(authAccount.getMerchantNum());
        // 从数据库中直接取加密密码
        String encodedPassword = authAccount.getPassword();
        userSpecificDetails.setPassword(encodedPassword);
        userSpecificDetails.setIdentity(new DefaultUserIdentity(authAccount.getAccountType(), authAccount.getId()));

        // 添加角色鉴权以及权限鉴权，每次访问带有权限限制的接口时就会验证，拥有对应权限code的话才可以正常访问。
        List<GrantedAuthority> authorities = new ArrayList<>();
        UserGrantedAuthority grantedAuthority = new UserGrantedAuthority();
        grantedAuthority.setType(authAccount.getAccountType());
        grantedAuthority.setRank(authAccount.getAccountRank());
        grantedAuthority.setApp(Strings.ASTERISK); // 通配符'*'
        grantedAuthority.setPermissions(authAccountDto.getRoleCodes());
        authorities.add(grantedAuthority);
        userSpecificDetails.setAuthorities(authorities);

        // 账户状态配置
        userSpecificDetails.setEnabled(authAccount.getEnabled());
        userSpecificDetails.setAccountNonLocked(true);
        userSpecificDetails.setAccountNonExpired(true);
        userSpecificDetails.setCredentialsNonExpired(true);
        return userSpecificDetails;
    }

}
