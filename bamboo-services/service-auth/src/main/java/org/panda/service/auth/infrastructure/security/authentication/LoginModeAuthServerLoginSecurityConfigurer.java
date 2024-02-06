package org.panda.service.auth.infrastructure.security.authentication;

import org.panda.tech.core.web.config.LoginModeEnum;
import org.panda.tech.security.web.authentication.LoginModeAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 支持多种登录方式的Auth服务端登录安全配置器
 */
@Component
public class LoginModeAuthServerLoginSecurityConfigurer extends
        AbstractAuthServerLoginSecurityConfigurer<LoginModeAuthenticationFilter, LoginModeAuthServerAuthenticationProvider> {

    @Override
    protected LoginModeAuthenticationFilter createProcessingFilter() {
        return new LoginModeAuthenticationFilter(getApplicationContext());
    }

    @Bean
    public AuthAccountAuthenticationTokenResolver defaultAuthenticationTokenResolver() {
        return new AuthAccountAuthenticationTokenResolver(LoginModeEnum.ACCOUNT.getValue());
    }

}
