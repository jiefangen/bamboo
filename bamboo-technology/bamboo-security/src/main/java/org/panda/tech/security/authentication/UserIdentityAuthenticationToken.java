package org.panda.tech.security.authentication;

import org.panda.tech.core.spec.user.UserIdentity;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

/**
 * 用户标识的认证令牌
 */
public class UserIdentityAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -3200058566123255631L;

    public UserIdentityAuthenticationToken(UserIdentity<?> userIdentity) {
        super(Collections.emptyList());
        super.setAuthenticated(true);
        setDetails(userIdentity);
    }

    @Override
    public Object getPrincipal() {
        return getDetails();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new UnsupportedOperationException();
    }

}
