package org.panda.business.official.infrastructure.security;

import org.panda.bamboo.common.constant.Commons;
import org.panda.business.official.infrastructure.security.authentication.LoginAuthenticationProvider;
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
        return new DefaultAuthenticationTokenResolver(Commons.COMMON_DEFAULT);
    }

    @Bean
    public SmsAuthenticationTokenResolver smsAuthenticationTokenResolver() {
        return new SmsAuthenticationTokenResolver(Commons.COMMON_SMS);
    }

    @Override
    protected UsernamePasswordAuthenticationFilter createProcessingFilter() {
        return new LoginModeAuthenticationFilter(super.getApplicationContext());
    }

}
