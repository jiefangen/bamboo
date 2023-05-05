package org.panda.tech.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 未经认证的认证令牌
 */
public class UnauthenticatedAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 7340231278504806289L;

    private Object principal;
    private Object credentials;

    public UnauthenticatedAuthenticationToken(Object principal, Object credentials) {
        super(null);
        super.setAuthenticated(false);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public final void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

}
