package org.panda.business.official.infrastructure.security;

import org.panda.tech.security.config.support.WebSecurityConfigurerSupport;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * WebMvc安全配置器
 *
 * @author fangen
 **/
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerSupport {
}
