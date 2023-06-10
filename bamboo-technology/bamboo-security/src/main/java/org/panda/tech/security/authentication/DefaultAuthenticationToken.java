package org.panda.tech.security.authentication;

/**
 * 默认用户名密码认证令牌
 */
public class DefaultAuthenticationToken extends UnauthenticatedAuthenticationToken {

    private static final long serialVersionUID = -1206641448564918380L;

    public DefaultAuthenticationToken(String username, String password) {
        super(username, password);
    }

    public String getUsername() {
        return (String) getPrincipal();
    }

    public String getPassword() {
        return (String) getCredentials();
    }

}
