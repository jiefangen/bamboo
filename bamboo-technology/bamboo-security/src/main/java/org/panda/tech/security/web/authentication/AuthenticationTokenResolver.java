package org.panda.tech.security.web.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录认证令牌解决器
 */
public interface AuthenticationTokenResolver<T extends AbstractAuthenticationToken> {

    /**
     * 获取当前构建器对应的登录方式，返回空表示作为默认登录方式
     *
     * @return 登录方式
     */
    String getLoginMode();

    T resolveAuthenticationToken(HttpServletRequest request);

}
