package org.panda.tech.security.config;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.mvc.view.exception.resolver.ViewResolvableExceptionResolver;
import org.panda.tech.security.web.endpoint.RedirectControllerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import java.util.Collection;
import java.util.Collections;

/**
 * WEB视图层安全配置支持
 */
public abstract class WebViewSecurityConfigurerSupport extends WebMvcSecurityConfigurerSupport {

    @Autowired
    private ViewResolvableExceptionResolver viewResolvableExceptionResolver;
    @Autowired
    private WebMvcProperties mvcProperties;

    @Bean
    @Override
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = (AccessDeniedHandlerImpl) super.accessDeniedHandler();
        String prefix = this.mvcProperties.getView().getPrefix();
        if (StringUtils.isNotBlank(prefix)) {
            prefix = NetUtil.standardizeUri(prefix);
        } else {
            prefix = Strings.EMPTY;
        }
        String businessErrorPath = this.viewResolvableExceptionResolver.getBusinessErrorPath();
        if (StringUtils.isNotBlank(businessErrorPath)) {
            businessErrorPath = NetUtil.standardizeUri(businessErrorPath);
        } else {
            businessErrorPath = Strings.EMPTY;
        }
        String errorPage = prefix + businessErrorPath + this.mvcProperties.getView().getSuffix();
        if (StringUtils.isNotBlank(errorPage)) {
            accessDeniedHandler.setErrorPage(errorPage);
        }
        return accessDeniedHandler;
    }

    /**
     * 获取安全框架忽略的URL ANT样式集合
     *
     * @return 安全框架忽略的URL ANT样式集合
     */
    @Override
    protected Collection<String> getIgnoringAntPatterns() {
        Collection<String> patterns = super.getIgnoringAntPatterns();
        patterns.add(getIgnoringAntPatternFromController(RedirectControllerSupport.class));
        // 静态资源全部忽略
        String staticPathPattern = this.mvcProperties.getStaticPathPattern();
        if (StringUtils.isNotBlank(staticPathPattern)) {
            String[] staticPathPatterns = StringUtil.splitAndTrim(staticPathPattern, Strings.COMMA);
            Collections.addAll(patterns, staticPathPatterns);
        }
        return patterns;
    }

}
