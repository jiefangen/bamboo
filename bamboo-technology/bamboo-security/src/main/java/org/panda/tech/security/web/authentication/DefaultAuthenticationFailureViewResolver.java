package org.panda.tech.security.web.authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认的登录认证失败的视图解决器
 */
public class DefaultAuthenticationFailureViewResolver implements AuthenticationFailureViewResolver {

    @Override
    public String resolveFailureView(HttpServletRequest request) {
        return "/login";
    }

}
