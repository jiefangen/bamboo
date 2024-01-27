package org.panda.tech.security.user;

import org.panda.tech.core.spec.user.UserIdentity;
import org.panda.tech.security.util.SecurityUtil;

/**
 * 默认用户特性细节提供者
 */
public class DefaultUserDetailsProvider implements UserDetailsProvider {

    @Override
    public <I extends UserIdentity<?>> I getUserIdentity() {
        return SecurityUtil.getAuthorizedUserIdentity();
    }

    @Override
    public <D extends UserSpecificDetails<?>> D getUserSpecificDetails() {
        return SecurityUtil.getAuthorizedUserDetails();
    }

}
