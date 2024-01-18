package org.panda.tech.core.web.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("bamboo.web.security")
public class WebSecurityProperties {

    private boolean csrfEnabled;
    private List<String> ignoringPatterns;
    /**
     * 没有权限注解的处理方法是否允许匿名访问，默认为false。
     * 注意：设置为true会带来安全隐患，忘记配置权限注解的处理方法将可匿名访问
     */
    private boolean anonymousWithoutAnnotation;
    /**
     * JWT认证鉴权类型，默认使用内部鉴定方式。
     * 设置external，鉴定外部应用提供更为安全的加密方式
     */
    private String jwtAuthFilterType;

    public boolean isCsrfEnabled() {
        return this.csrfEnabled;
    }

    public void setCsrfEnabled(boolean csrfEnabled) {
        this.csrfEnabled = csrfEnabled;
    }

    public List<String> getIgnoringPatterns() {
        return this.ignoringPatterns;
    }

    public void setIgnoringPatterns(List<String> ignoringPatterns) {
        this.ignoringPatterns = ignoringPatterns;
    }

    public boolean isAnonymousWithoutAnnotation() {
        return anonymousWithoutAnnotation;
    }

    public void setAnonymousWithoutAnnotation(boolean anonymousWithoutAnnotation) {
        this.anonymousWithoutAnnotation = anonymousWithoutAnnotation;
    }

    public String getJwtAuthFilterType() {
        return jwtAuthFilterType;
    }

    public void setJwtAuthFilterType(String jwtAuthFilterType) {
        this.jwtAuthFilterType = jwtAuthFilterType;
    }
}
