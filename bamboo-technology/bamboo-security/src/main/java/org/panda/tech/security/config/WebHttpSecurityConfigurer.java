package org.panda.tech.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * HttpSecurity配置器，用于附加安全配置
 */
public interface WebHttpSecurityConfigurer {

    void configure(HttpSecurity http) throws Exception;

}
