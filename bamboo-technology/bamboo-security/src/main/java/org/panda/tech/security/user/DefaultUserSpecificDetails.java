package org.panda.tech.security.user;

import org.panda.tech.core.spec.user.DefaultUserIdentity;

/**
 * 默认的用户特性细节
 */
public class DefaultUserSpecificDetails extends SimpleUserSpecificDetails<DefaultUserIdentity> {

    private static final long serialVersionUID = 9017622032464226982L;

    private String password;

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
