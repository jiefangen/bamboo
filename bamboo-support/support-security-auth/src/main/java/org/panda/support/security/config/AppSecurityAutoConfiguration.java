package org.panda.support.security.config;

import org.panda.support.security.AuthenticationFilter;
import org.panda.support.security.AuthorizationInterceptor;
import org.panda.support.security.authority.AppSecurityMetadataSource;
import org.panda.support.security.authority.AuthoritiesAppExecutor;
import org.panda.support.security.executor.AuthoritiesAppExecutorImpl;
import org.panda.support.security.executor.strategy.IndependentAuthStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 应用安全元数据源配置
 *
 * @author fangen
 **/
@Import({IndependentAuthStrategy.class, AuthoritiesAppExecutorImpl.class})
public class AppSecurityAutoConfiguration {

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
