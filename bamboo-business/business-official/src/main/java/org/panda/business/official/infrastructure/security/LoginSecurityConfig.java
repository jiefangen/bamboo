package org.panda.business.official.infrastructure.security;

import org.panda.business.official.infrastructure.security.authentication.LoginAuthenticationProvider;
import org.panda.tech.security.config.LoginSecurityConfigurerSupport;
import org.panda.tech.security.web.authentication.LoginAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 服务端登录安全配置器
 */
@Component
public class LoginSecurityConfig extends LoginSecurityConfigurerSupport<UsernamePasswordAuthenticationFilter, LoginAuthenticationProvider> {

    @Override
    protected UsernamePasswordAuthenticationFilter createProcessingFilter() {
        return new LoginAuthenticationFilter(super.getApplicationContext());
    }

}
