package org.panda.business.helper.app.infrastructure.security.auth;

import org.panda.business.helper.app.infrastructure.security.user.HelperUserIdentity;
import org.panda.tech.auth.authentication.LoginInfo;
import org.panda.tech.auth.authentication.LogoutInfo;
import org.panda.tech.auth.authentication.token.AuthenticationToken;
import org.panda.tech.auth.authority.AuthorizationInfo;
import org.panda.tech.auth.realm.Realm;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.exception.business.HandleableException;

/**
 * 助手APP校验领域
 *
 * @author fangen
 * @since JDK 11 2024/7/17
 **/
public class HelperAppRealm implements Realm<HelperUserIdentity> {

    @Override
    public Class<HelperUserIdentity> getUserClass() {
        return HelperUserIdentity.class;
    }

    @Override
    public LoginInfo getLoginInfo(AuthenticationToken loginToken) throws HandleableException {
        return null;
    }

    @Override
    public AuthorizationInfo getAuthorizationInfo(HelperUserIdentity user) {
        return null;
    }

    @Override
    public LogoutInfo getLogoutInfo(HelperUserIdentity user) throws BusinessException {
        return null;
    }
}
