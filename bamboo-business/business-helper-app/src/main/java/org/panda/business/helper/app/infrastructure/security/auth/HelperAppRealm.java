package org.panda.business.helper.app.infrastructure.security.auth;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.model.tuple.Binary;
import org.panda.business.helper.app.common.constant.ProjectConstants;
import org.panda.business.helper.app.infrastructure.security.auth.user.HelperUser;
import org.panda.tech.auth.authentication.DefaultLoginInfo;
import org.panda.tech.auth.authentication.DefaultLogoutInfo;
import org.panda.tech.auth.authentication.LoginInfo;
import org.panda.tech.auth.authentication.LogoutInfo;
import org.panda.tech.auth.authentication.token.AuthenticationToken;
import org.panda.tech.auth.authentication.token.UsernamePasswordToken;
import org.panda.tech.auth.authority.AuthorizationInfo;
import org.panda.tech.auth.realm.Realm;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.exception.business.HandleableException;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

/**
 * 助手APP校验领域
 *
 * @author fangen
 * @since JDK 11 2024/7/17
 **/
@Component
public class HelperAppRealm implements Realm<HelperUser> {

    @Override
    public Class<HelperUser> getUserClass() {
        return HelperUser.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LoginInfo getLoginInfo(AuthenticationToken loginToken) throws HandleableException {
        String identity = (String) loginToken.getPrincipal();
        HelperUser helperUser = null;
        if (loginToken instanceof UsernamePasswordToken) {
            String password = (String) loginToken.getCredentials();

        } else { // 短信验证码方式
            String mobilePhone = (String) loginToken.getPrincipal();
            Binary<Long, String> binary = (Binary<Long, String>) loginToken.getCredentials();
            Long verifyId = binary.getLeft();
            String code = binary.getRight();
        }
        DefaultLoginInfo loginInfo = new DefaultLoginInfo(helperUser);
        Cookie identityCookie = WebHttpUtil.createCookie(ProjectConstants.COOKIE_IDENTITY, identity,
                Strings.SLASH, Integer.MAX_VALUE, true);
        loginInfo.addCookie(identityCookie);
        return loginInfo;
    }

    @Override
    public AuthorizationInfo getAuthorizationInfo(HelperUser user) {
        return null;
    }

    @Override
    public LogoutInfo getLogoutInfo(HelperUser user) throws BusinessException {
        return new DefaultLogoutInfo(false);
    }
}
