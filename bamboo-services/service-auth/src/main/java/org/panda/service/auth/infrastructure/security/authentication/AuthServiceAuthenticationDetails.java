package org.panda.service.auth.infrastructure.security.authentication;

import java.io.Serializable;

public class AuthServiceAuthenticationDetails implements Serializable {

    private static final long serialVersionUID = 2601595191074890901L;

    private String service;
    private String scope;
    private String ip;

    public AuthServiceAuthenticationDetails(String service, String scope, String ip) {
        this.service = service;
        this.scope = scope;
        this.ip = ip;
    }

    public String getService() {
        return this.service;
    }

    public String getScope() {
        return this.scope;
    }

    public String getIp() {
        return this.ip;
    }
}
