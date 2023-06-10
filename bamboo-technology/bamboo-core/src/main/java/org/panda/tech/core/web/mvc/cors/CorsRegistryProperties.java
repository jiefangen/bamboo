package org.panda.tech.core.web.mvc.cors;

import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("bamboo.web.cors")
public class CorsRegistryProperties {

    private String pathPattern = "/**";

    private String[] allowedOrigins = {}; // 不能配置允许所有来源站点的*，必须一个个站点加入，否则无效

    private String[] allowedMethods = { Strings.ASTERISK };

    private String[] allowedHeaders = { Strings.ASTERISK };

    private String[] exposedHeaders = {};

    private Long maxAge;

    public String getPathPattern() {
        return this.pathPattern;
    }

    public void setPathPattern(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    public String[] getAllowedOrigins() {
        return this.allowedOrigins;
    }

    public void setAllowedOrigins(String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public String[] getAllowedMethods() {
        return this.allowedMethods;
    }

    public void setAllowedMethods(String[] allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public String[] getAllowedHeaders() {
        return this.allowedHeaders;
    }

    public void setAllowedHeaders(String[] allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public String[] getExposedHeaders() {
        return this.exposedHeaders;
    }

    public void setExposedHeaders(String[] exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public Long getMaxAge() {
        return this.maxAge;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

}
