package org.panda.tech.security.access;

import org.panda.tech.security.user.UserSpecificDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 用户特性细节的认证令牌
 */
public class UserSpecificDetailsAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 5719790668377346866L;

    private String ip;

    public UserSpecificDetailsAuthenticationToken(UserSpecificDetails<?> details) {
        super(details.getAuthorities());
        super.setAuthenticated(true);
        setDetails(details);
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public Object getPrincipal() {
        return ((UserSpecificDetails<?>) getDetails()).getIdentity();
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
