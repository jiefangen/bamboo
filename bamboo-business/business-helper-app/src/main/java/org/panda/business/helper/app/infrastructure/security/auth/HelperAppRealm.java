package org.panda.business.helper.app.infrastructure.security.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.business.helper.app.common.constant.ProjectConstants;
import org.panda.business.helper.app.common.utils.AppPassUtils;
import org.panda.business.helper.app.model.entity.AppUser;
import org.panda.business.helper.app.model.vo.UserInfo;
import org.panda.business.helper.app.service.AppUserService;
import org.panda.tech.auth.authentication.DefaultLoginInfo;
import org.panda.tech.auth.authentication.DefaultLogoutInfo;
import org.panda.tech.auth.authentication.LoginInfo;
import org.panda.tech.auth.authentication.LogoutInfo;
import org.panda.tech.auth.authentication.token.AuthenticationToken;
import org.panda.tech.auth.authentication.token.SmsVerifyToken;
import org.panda.tech.auth.authentication.token.UsernamePasswordToken;
import org.panda.tech.auth.authority.AuthorizationInfo;
import org.panda.tech.auth.authority.DefaultAuthorizationInfo;
import org.panda.tech.auth.authority.PerConstants;
import org.panda.tech.auth.realm.RememberMeRealm;
import org.panda.tech.auth.authentication.enums.AuthRoleCode;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.exception.business.auth.AuthConstants;
import org.panda.tech.core.exception.business.param.RequiredParamException;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

/**
 * 助手APP校验领域
 *
 * @author fangen
 * @since JDK 11 2024/7/17
 **/
@Component
public class HelperAppRealm implements RememberMeRealm<HelperUser> {

    @Autowired
    private AppUserService appUserService;

    @Override
    public Class<HelperUser> getUserClass() {
        return HelperUser.class;
    }

    @Override
    public HelperUser getLoginUser(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        HelperUser helperUser = new HelperUser();
        UserInfo userInfo = appUserService.getUserByToken(token);
        helperUser.setUserInfo(userInfo);
        helperUser.setId(userInfo.getUserId());
        return helperUser;
    }

    @Override
    public LoginInfo getLoginInfo(AuthenticationToken loginToken) throws BusinessException {
        HelperUser helperUser = new HelperUser();
        UserInfo userInfo = null;
        String identity = null; // 唯一身份标识
        if (loginToken instanceof UsernamePasswordToken) { // 用户名密码登录方式
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) loginToken;
            identity = usernamePasswordToken.getUsername();
            String password = usernamePasswordToken.getPassword();
            if (StringUtils.isEmpty(identity) || StringUtils.isEmpty(password)) {
                throw new RequiredParamException();
            }
            // 查询登录用户
            LambdaQueryWrapper<AppUser> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(AppUser::getUsername, identity);
            AppUser appUser = appUserService.getOne(queryWrapper, false);
            if (ObjectUtils.isEmpty(appUser)) {
                throw new BusinessException(AuthConstants.USERNAME_NOT_EXIST);
            }
            // 密码校验
            String encodedPassword = AppPassUtils.encryptPassword(password, appUser.getSalt());
            if (!encodedPassword.equals(appUser.getPassword())) {
                throw new BusinessException(AuthConstants.PWD_WRONG);
            }
            userInfo = appUserService.buildUserInfo(appUser);
        } else if (loginToken instanceof SmsVerifyToken) { // 短信验证码登录方式
            SmsVerifyToken smsVerifyToken = (SmsVerifyToken) loginToken;
            identity = smsVerifyToken.getMobilePhone();
            Long verifyId = smsVerifyToken.getVerifyId();
            String code = smsVerifyToken.getCode();
            // 手机号短信验证码校验
        } else { // 默认登录方式
            Object principal = loginToken.getPrincipal();
            if (principal instanceof UserInfo) {
                userInfo = (UserInfo) principal;
                identity = userInfo.getUsername();
            }
        }
        if (ObjectUtils.isNotEmpty(userInfo)) {
            helperUser.setUserInfo(userInfo);
            helperUser.setId(userInfo.getUserId());
        }
        DefaultLoginInfo loginInfo = new DefaultLoginInfo(helperUser);
        Cookie identityCookie = WebHttpUtil.createCookie(ProjectConstants.COOKIE_IDENTITY, identity,
                Strings.SLASH, Integer.MAX_VALUE, true);
        loginInfo.addCookie(identityCookie);
        return loginInfo;
    }

    @Override
    public AuthorizationInfo getAuthorizationInfo(HelperUser user) {
        DefaultAuthorizationInfo authorizationInfo = new DefaultAuthorizationInfo(true);
        if (ObjectUtils.isNotEmpty(user) && ObjectUtils.isNotEmpty(user.getUserInfo())) {
            UserInfo userInfo = user.getUserInfo();
            if (AuthRoleCode.isTopRole("")) {
                // 管理员可添加*通配符权限，即可访问所有资源
                authorizationInfo.addRole(Strings.ASTERISK);
                authorizationInfo.addPermission(Strings.ASTERISK);
            } else {
                // 普通用户权限资源
                authorizationInfo.addRole(PerConstants.ROLE_GENERAL);
                authorizationInfo.addRole(PerConstants.ROLE_ACCOUNT);
                // 添加用户等级权限
                Integer userRank = userInfo.getUserRank();
                for (int i = 1; i <= userRank; i++) {
                    authorizationInfo.addPermission(getPerRank(i));
                }
            }
        }
        // 访客只需要登录过的用户即可拥有
        authorizationInfo.addRole(PerConstants.ROLE_CUSTOMER);
        return authorizationInfo;
    }

    /**
     * 获取用户等级权限
     */
    private String getPerRank(Integer userRank) {
        String perRank = PerConstants.RANK_1;
        if (ObjectUtils.isNotEmpty(userRank)) {
            switch (userRank) {
                case 1:
                    perRank = PerConstants.RANK_1;
                    break;
                case 2:
                    perRank = PerConstants.RANK_2;
                    break;
                case 3:
                    perRank = PerConstants.RANK_3;
                    break;
                }
        }
        return perRank;
    }

    @Override
    public LogoutInfo getLogoutInfo(HelperUser user) throws BusinessException {
        return new DefaultLogoutInfo(false);
    }

}
