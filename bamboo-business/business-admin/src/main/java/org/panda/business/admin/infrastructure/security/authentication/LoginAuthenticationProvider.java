package org.panda.business.admin.infrastructure.security.authentication;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.panda.business.admin.common.constant.SystemConstants;
import org.panda.business.admin.modules.common.manager.SettingsManager;
import org.panda.business.admin.modules.common.config.SettingsKeys;
import org.panda.business.admin.modules.monitor.service.SysUserTokenService;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import org.panda.tech.security.authentication.*;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.panda.tech.security.user.UserSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 支持登录服务端认证提供者
 */
@Component
public class LoginAuthenticationProvider extends AbstractAuthenticationProvider<AbstractAuthenticationToken> {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserSpecificDetailsService userDetailsService;
    @Autowired
    private SysUserTokenService userTokenService;
    @Autowired
    private SettingsManager settingsManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        DefaultUserSpecificDetails userSpecificDetails = (DefaultUserSpecificDetails) userDetailsService.loadUserByUsername(username);
        if (authentication instanceof DefaultAuthenticationToken) { // 用户名密码令牌方式
            if (!userSpecificDetails.isEnabled()) { // 账户禁用状态拦截
                throw new DisabledException(SystemConstants.USER_DISABLED);
            }
            if (!userSpecificDetails.isAccountNonLocked()) {
                throw new LockedException(SystemConstants.USER_LOCKED);
            }
            if (!passwordEncoder.matches(password, userSpecificDetails.getPassword())) {
                throw new BadCredentialsException(SystemConstants.PWD_WRONG);
            }
        }
        if (authentication instanceof SmsVerifyCodeAuthenticationToken) { // 短信令牌登录方式
        }
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = webAuthenticationDetails.getRemoteAddress();
        UserSpecificDetailsAuthenticationToken authenticationToken = new UserSpecificDetailsAuthenticationToken(userSpecificDetails);
        authenticationToken.setIp(remoteAddress);
        // 在线用户登录限制
        this.onlineUserLimit(username);
        return authenticationToken;
    }

    /**
     * 在线用户登录限制
     * 同时在线用户/离线也算半在线用户
     */
    private void onlineUserLimit(String username) {
        LambdaQueryWrapper<SysUserToken> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserToken::getIdentity, username);
        queryWrapper.in(SysUserToken::getStatus, 1, 2);
        queryWrapper.orderByAsc(SysUserToken::getCreateTime);
        List<SysUserToken> userTokens = userTokenService.list(queryWrapper);
        int userLimit = 1;
        Optional<String> userLimitOptional = settingsManager.getParamValueByKey(SettingsKeys.ONLINE_LIMIT);
        if (userLimitOptional.isPresent()) {
            userLimit = Integer.parseInt(userLimitOptional.get());
        }
        if (CollectionUtils.isNotEmpty(userTokens)) {
            if (userTokens.size() >= userLimit) {
                // 踢出登录时间最早的那个用户
                SysUserToken firstUserToken = userTokens.get(0);
                firstUserToken.setStatus(4);
                userTokenService.updateById(firstUserToken);
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UnauthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
