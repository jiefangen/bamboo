package org.panda.service.doc.config;

import org.panda.tech.core.web.mvc.servlet.filter.RequestLogFilter;
import org.panda.tech.core.web.mvc.support.WebMvcConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WebMvc配置器
 *
 * @author fangen
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurerSupport {
    /**
     * Web请求日志过滤器
     */
    @Bean
    public RequestLogFilter requestLogFilter() {
        return new RequestLogFilter("/file/**");
    }
}
