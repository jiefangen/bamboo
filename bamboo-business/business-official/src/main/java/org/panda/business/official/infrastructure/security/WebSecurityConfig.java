package org.panda.business.official.infrastructure.security;

import org.panda.business.official.infrastructure.security.authentication.LoginAuthenticationProvider;
import org.panda.tech.security.config.WebSecurityConfigurerSupport;
import org.panda.tech.security.user.UserSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

/**
 * Web安全配置器
 *
 * @author fangen
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerSupport {

    @Autowired
    private UserSpecificDetailsService userDetailsService;
    @Autowired
    private LoginAuthenticationProvider loginAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginAuthenticationProvider);
        auth.userDetailsService(userDetailsService);
    }

}
