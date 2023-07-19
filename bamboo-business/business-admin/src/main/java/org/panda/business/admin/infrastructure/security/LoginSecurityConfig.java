package org.panda.business.admin.infrastructure.security;

import org.panda.business.admin.infrastructure.security.authentication.LoginAuthenticationProvider;
import org.panda.tech.core.web.config.LoginModeEnum;
import org.panda.tech.security.config.LoginSecurityConfigurerSupport;
import org.panda.tech.security.web.authentication.DefaultAuthenticationTokenResolver;
import org.panda.tech.security.web.authentication.LoginModeAuthenticationFilter;
import org.panda.tech.security.web.authentication.SmsAuthenticationTokenResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 服务端登录安全配置器
 */
@Component
public class LoginSecurityConfig extends LoginSecurityConfigurerSupport<UsernamePasswordAuthenticationFilter, LoginAuthenticationProvider> {

    @Bean
    public DefaultAuthenticationTokenResolver defaultAuthenticationTokenResolver() {
        return new DefaultAuthenticationTokenResolver(LoginModeEnum.ACCOUNT.getValue());
    }

    @Bean
    public SmsAuthenticationTokenResolver smsAuthenticationTokenResolver() {
        return new SmsAuthenticationTokenResolver(LoginModeEnum.SMS.getValue());
    }

    @Override
    protected UsernamePasswordAuthenticationFilter createProcessingFilter() {
        return new LoginModeAuthenticationFilter(super.getApplicationContext());
    }

}
