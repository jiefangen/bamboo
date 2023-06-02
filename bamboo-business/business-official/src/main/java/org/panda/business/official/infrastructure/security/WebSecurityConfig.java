package org.panda.business.official.infrastructure.security;

import org.panda.tech.security.config.WebSecurityConfigurerSupport;
import org.panda.tech.security.user.UserSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Web安全配置器
 *
 * @author fangen
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerSupport {

    @Autowired
    private UserSpecificDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        userDetailsService.setPasswordEncoder(passwordEncoder());
        auth.userDetailsService(userDetailsService);
    }

}
