package org.panda.business.admin.infrastructure.security;

import org.panda.tech.security.config.WebSecurityConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Web安全配置器
 *
 * @author fangen
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerSupport {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
