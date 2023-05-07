package org.panda.tech.security.web.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 抽象的登录认证令牌解决器
 *
 * @param <T> 令牌类型
 */
public abstract class AbstractAuthenticationTokenResolver<T extends AbstractAuthenticationToken>
        implements AuthenticationTokenResolver<T> {

    private String loginMode;

    public AbstractAuthenticationTokenResolver(String loginMode) {
        this.loginMode = loginMode;
    }

    @Override
    public String getLoginMode() {
        return this.loginMode;
    }

}
