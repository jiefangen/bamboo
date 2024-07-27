package org.panda.business.helper.app.infrastructure.security.auth;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.business.helper.app.common.constant.ProjectConstants;
import org.panda.business.helper.app.infrastructure.security.auth.user.HelperUser;
import org.panda.business.helper.app.model.vo.UserInfo;
import org.panda.tech.auth.authentication.DefaultLoginInfo;
import org.panda.tech.auth.authentication.DefaultLogoutInfo;
import org.panda.tech.auth.authentication.LoginInfo;
import org.panda.tech.auth.authentication.LogoutInfo;
import org.panda.tech.auth.authentication.token.AuthenticationToken;
import org.panda.tech.auth.authentication.token.SmsVerifyToken;
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

    @Override
    public LoginInfo getLoginInfo(AuthenticationToken loginToken) throws HandleableException {
        String identity = null; // 唯一身份标识
        HelperUser helperUser = new HelperUser();
        if (loginToken instanceof UsernamePasswordToken) { // 用户名密码登录方式
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) loginToken;
            identity = usernamePasswordToken.getUsername();
            String password = usernamePasswordToken.getPassword();
        } else if (loginToken instanceof SmsVerifyToken) { // 短信验证码登录方式
            SmsVerifyToken smsVerifyToken = (SmsVerifyToken) loginToken;
            identity = smsVerifyToken.getMobilePhone();
            Long verifyId = smsVerifyToken.getVerifyId();
            String code = smsVerifyToken.getCode();
        } else { // 默认登录方式
            Object principal = loginToken.getPrincipal();
            if (principal instanceof UserInfo) {
                UserInfo userInfo = (UserInfo) principal;
                helperUser.setUserInfo(userInfo);
                helperUser.setId(userInfo.getUserId());
                identity = userInfo.getUsername();
            }
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
