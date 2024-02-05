package org.panda.service.auth.infrastructure.security.authorization;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.panda.service.auth.common.constant.AuthConstants;
import org.panda.service.auth.infrastructure.security.authentication.AuthServerLoginAuthenticator;
import org.panda.service.auth.infrastructure.security.service.AuthServerExceptionCodes;
import org.panda.service.auth.model.entity.AppServer;
import org.panda.service.auth.service.AppServerService;
import org.panda.tech.security.authentication.DefaultAuthenticationToken;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.panda.tech.security.user.UserSpecificDetails;
import org.panda.tech.security.user.UserSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Auth登录验证器
 *
 * @author fangen
 **/
@Component
public class AuthLoginAuthenticator implements AuthServerLoginAuthenticator<DefaultAuthenticationToken> {

    @Autowired
    private UserSpecificDetailsService specificDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppServerService appServerService;

    @Override
    public UserSpecificDetails<?> authenticate(String appName, String scope, DefaultAuthenticationToken token) {
        String username = token.getUsername();
        String password = token.getPassword();
        DefaultUserSpecificDetails userSpecificDetails =
                (DefaultUserSpecificDetails) specificDetailsService.loadUserByUsername(username);
        if (!userSpecificDetails.isEnabled()) { // 账户禁用状态拦截
            throw new DisabledException(AuthConstants.ACCOUNT_DISABLED);
        }
        if (!userSpecificDetails.isAccountNonLocked()) {
            throw new LockedException(AuthConstants.ACCOUNT_LOCKED);
        }
        if (!passwordEncoder.matches(password, userSpecificDetails.getPassword())) {
            throw new BadCredentialsException(AuthConstants.PWD_WRONG);
        }
        // 应用服务有效性验证
        if (!appServerVerification(appName, scope)) {
            throw new BadCredentialsException(AuthServerExceptionCodes.INVALID_SERVICE);
        }
        return userSpecificDetails;
    }

    /**
     * 应用服务验证
     *
     * @param appName 应用服务名
     * @param scope 范围
     * @return true-验证通过；false-验证失败
     */
    private boolean appServerVerification(String appName, String scope) {
        LambdaQueryWrapper<AppServer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppServer::getAppName, appName);
        queryWrapper.eq(AppServer::getStatus, 1);
        return appServerService.count(queryWrapper) > 0;
    }
}
