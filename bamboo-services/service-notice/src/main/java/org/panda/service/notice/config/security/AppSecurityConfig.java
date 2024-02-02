package org.panda.service.notice.config.security;

import org.panda.service.notice.config.security.strategy.IndependentAuthStrategy;
import org.panda.support.cloud.core.security.AuthenticationFilter;
import org.panda.support.cloud.core.security.AuthorizationInterceptor;
import org.panda.support.cloud.core.security.authority.AppSecurityMetadataSource;
import org.panda.support.cloud.core.security.authority.AuthoritiesAppExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 应用安全元数据源配置
 *
 * @author fangen
 **/
@Configuration
public class AppSecurityConfig {

    @Autowired
    private AuthoritiesAppExecutor authoritiesAppExecutor;

    @Bean
    public AppSecurityMetadataSource appSecurityMetadataSource() {
        return new AppSecurityMetadataSource(authoritiesAppExecutor);
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(IndependentAuthStrategy.class);
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor(IndependentAuthStrategy.class);
    }

}
