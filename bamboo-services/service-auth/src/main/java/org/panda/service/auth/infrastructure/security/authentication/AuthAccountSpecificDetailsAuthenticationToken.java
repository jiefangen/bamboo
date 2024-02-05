package org.panda.service.auth.infrastructure.security.authentication;

import org.panda.tech.security.authentication.UserSpecificDetailsAuthenticationToken;
import org.panda.tech.security.user.UserSpecificDetails;

/**
 * Auth用户特性细节鉴权令牌
 */
public class AuthAccountSpecificDetailsAuthenticationToken extends UserSpecificDetailsAuthenticationToken {

    private static final long serialVersionUID = 955845071338145039L;

    private String service;
    private String scope;

    public AuthAccountSpecificDetailsAuthenticationToken(UserSpecificDetails<?> details, String service, String scope,
                                                         String ip) {
        super(details);
        this.service = service;
        this.scope = scope;
        setIp(ip);
    }

    public String getService() {
        return this.service;
    }

    public String getScope() {
        return this.scope;
    }
}
