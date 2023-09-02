package org.panda.tech.security.web.authentication;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.SpringUtil;
import org.panda.tech.core.web.config.meta.ApiMetaProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 登录认证过滤器
 */
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static void applySuccessTargetUrlParameter(ApplicationContext context,
            AuthenticationSuccessHandler successHandler) {
        if (successHandler instanceof AbstractAuthenticationTargetUrlRequestHandler) {
            ApiMetaProperties apiMetaProperties = SpringUtil.getFirstBeanByClass(context, ApiMetaProperties.class);
            if (apiMetaProperties != null) {
                String successTargetUrlParameter = apiMetaProperties.getRedirectTargetUrlParameter();
                if (StringUtils.isNotBlank(successTargetUrlParameter)) {
                    ((AbstractAuthenticationTargetUrlRequestHandler) successHandler)
                            .setTargetUrlParameter(successTargetUrlParameter);
                }
            }
        }
    }

    public LoginAuthenticationFilter(ApplicationContext context) {
        AjaxAuthenticationSuccessHandler successHandler = SpringUtil.getFirstBeanByClass(context,
                AjaxAuthenticationSuccessHandler.class);
        if (successHandler != null) {
            setAuthenticationSuccessHandler(successHandler);
        }
        applySuccessTargetUrlParameter(context, getSuccessHandler());

        ResolvableExceptionAuthenticationFailureHandler failureHandler = SpringUtil
                .getFirstBeanByClass(context, ResolvableExceptionAuthenticationFailureHandler.class);
        if (failureHandler != null) {
            setAuthenticationFailureHandler(failureHandler); // 指定登录失败时的处理器
        }
    }

    @Override
    public AuthenticationSuccessHandler getSuccessHandler() {
        return super.getSuccessHandler();
    }

    @Override
    public AuthenticationFailureHandler getFailureHandler() {
        return super.getFailureHandler();
    }

}
