package org.panda.service.auth.common.config;

import org.panda.tech.core.jwt.encrypt.JwtGenerator;
import org.panda.tech.core.jwt.encrypt.JwtGeneratorImpl;
import org.panda.tech.core.jwt.encrypt.JwtParser;
import org.panda.tech.core.jwt.encrypt.JwtParserImpl;
import org.panda.tech.core.rpc.filter.RpcInvokerFilter;
import org.panda.tech.core.web.mvc.servlet.filter.RequestLogFilter;
import org.panda.tech.core.web.mvc.support.WebMvcConfigurerSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * WebMvc配置器
 *
 * @author fangen
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurerSupport {

    @Bean
    public RequestLogFilter requestLogFilter() {
        return new RequestLogFilter("/auth/**");
    }

    @Bean
    public RpcInvokerFilter rpcInvokerFilter() {
        return new RpcInvokerFilter();
    }

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 声明JWT解析器
     */
    @Bean
    @ConditionalOnMissingBean(JwtParser.class)
    public JwtParser jwtParser() {
        return new JwtParserImpl();
    }

    /**
     * 声明JWT生成器
     */
    @Bean
    @ConditionalOnMissingBean(JwtGenerator.class)
    public JwtGenerator jwtGenerator() {
        return new JwtGeneratorImpl();
    }

}
