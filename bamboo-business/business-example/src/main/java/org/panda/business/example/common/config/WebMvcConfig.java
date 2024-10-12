package org.panda.business.example.common.config;

import org.panda.tech.core.jwt.internal.resolver.DefaultInternalJwtResolver;
import org.panda.tech.core.jwt.internal.resolver.InternalJwtResolver;
import org.panda.tech.core.web.mvc.support.WebMvcConfigurerSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WebMvc配置器
 *
 * @author fangen
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurerSupport {

    @Bean
    @ConditionalOnMissingBean(InternalJwtResolver.class)
    public InternalJwtResolver internalJwtResolver() {
        return new DefaultInternalJwtResolver();
    }

}
