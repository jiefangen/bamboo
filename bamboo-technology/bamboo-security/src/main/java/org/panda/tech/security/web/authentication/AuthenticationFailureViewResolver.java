package org.panda.tech.security.web.authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录认证失败的视图解决器
 */
public interface AuthenticationFailureViewResolver {

    String resolveFailureView(HttpServletRequest request);

}
