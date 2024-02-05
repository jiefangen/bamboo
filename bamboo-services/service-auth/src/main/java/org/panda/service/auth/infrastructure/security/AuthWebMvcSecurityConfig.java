package org.panda.service.auth.infrastructure.security;

import org.panda.service.auth.infrastructure.security.authentication.login.AuthLoginAuthenticationSuccessHandler;
import org.panda.service.auth.infrastructure.security.authentication.logout.AuthServerLogoutHandler;
import org.panda.service.auth.infrastructure.security.authentication.logout.AuthServerLogoutSuccessHandler;
import org.panda.tech.security.config.WebHttpSecurityConfigurer;
import org.panda.tech.security.config.WebMvcSecurityConfigurerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Auth安全配置器
 *
 * @author fangen
 **/
@Configuration
public class AuthWebMvcSecurityConfig extends WebMvcSecurityConfigurerSupport {

    @Autowired
    private AuthServerLogoutHandler logoutHandler;

    @Bean
    public AuthLoginAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthLoginAuthenticationSuccessHandler();
    }

    @Bean
    public WebHttpSecurityConfigurer logoutConfigurer() {
        return http -> {
            http.logout().addLogoutHandler(AuthWebMvcSecurityConfig.this.logoutHandler);
        };
    }

    @Bean
    @Override
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new AuthServerLogoutSuccessHandler();
    }
}
