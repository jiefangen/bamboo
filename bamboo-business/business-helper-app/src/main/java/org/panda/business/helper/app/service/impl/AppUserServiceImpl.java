package org.panda.business.helper.app.service.impl;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.bamboo.common.constant.basic.Times;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.business.helper.app.common.constant.AppSourceType;
import org.panda.business.helper.app.common.constant.ProjectConstants;
import org.panda.business.helper.app.common.utils.AppPassUtils;
import org.panda.business.helper.app.infrastructure.security.AppSecurityUtils;
import org.panda.business.helper.app.infrastructure.security.UserIdentityToken;
import org.panda.business.helper.app.infrastructure.security.auth.HelperUser;
import org.panda.business.helper.app.infrastructure.security.auth.SubjectUtils;
import org.panda.business.helper.app.infrastructure.thirdparty.wechat.WechatMpManager;
import org.panda.business.helper.app.model.entity.AppUser;
import org.panda.business.helper.app.model.entity.AppUserToken;
import org.panda.business.helper.app.model.entity.AppUserWechat;
import org.panda.business.helper.app.model.params.AppLoginParam;
import org.panda.business.helper.app.model.params.UpdateUserParam;
import org.panda.business.helper.app.model.vo.UserInfo;
import org.panda.business.helper.app.repository.AppUserMapper;
import org.panda.business.helper.app.service.AppUserService;
import org.panda.business.helper.app.service.AppUserTokenService;
import org.panda.business.helper.app.service.AppUserWechatService;
import org.panda.support.openapi.model.EncryptedData;
import org.panda.support.openapi.model.WechatAppType;
import org.panda.support.openapi.model.WechatUser;
import org.panda.tech.shiro.authentication.token.AuthenticationToken;
import org.panda.tech.shiro.authentication.token.DefaultAuthToken;
import org.panda.tech.shiro.authentication.token.SmsVerifyToken;
import org.panda.tech.shiro.authentication.token.UsernamePasswordToken;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.exception.business.auth.AuthConstants;
import org.panda.tech.core.jwt.internal.InternalJwtConfiguration;
import org.panda.tech.core.web.config.LoginModeEnum;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * APP用户 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2024-06-05
 */
@Service
@Transactional
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {

    @Autowired
    private AppSecurityUtils appSecurityUtils;
    @Autowired
    private InternalJwtConfiguration jwtConfiguration;
    @Autowired
    private AppUserTokenService userTokenService;
    @Autowired
    private AppUserWechatService appUserWechatService;
    @Autowired
    private WechatMpManager wechatMpManager;

    @Override
    public RestfulResult<?> appLogin(AppLoginParam appLoginParam, HttpServletRequest request) {
        String username = appLoginParam.getUsername();
        LambdaQueryWrapper<AppUser> queryWrapper = Wrappers.lambdaQuery();
        String appSource = appSecurityUtils.getSourceHeader(request);
        WechatUser wechatUser = null;
        // 根据不同app来源查询对应用户授权信息
        if (Objects.equals(EnumValueHelper.getValue(AppSourceType.WECHAT_MINI), appSource)) {
            if (StringUtils.isEmpty(appLoginParam.getOpenid())) {
                try {
                    // 利用临时code获取openid、sessionKey等微信授权信息
                    wechatUser = wechatMpManager.loadUser(appLoginParam.getCode());
                    appLoginParam.setOpenid(wechatUser.getOpenid());
                } catch (BusinessException e) {
                    return RestfulResult.failure(e.getMessage());
                }
            }
            queryWrapper.eq(AppUser::getOpenid, appLoginParam.getOpenid());
        } else if (Objects.equals(EnumValueHelper.getValue(AppSourceType.ANDROID), appSource)) {
            queryWrapper.eq(AppUser::getUsername, username);
        } else {
            return RestfulResult.failure("Illegal app client");
        }
        queryWrapper.eq(AppUser::getSource, appSource);
        AppUser appUser = this.getOne(queryWrapper, false);
        if (ObjectUtils.isEmpty(appUser)) { // 用户不存在即自动注册
            appUser = addAppUser(appLoginParam, appSource);
        }
        // 获取到用户后进入登录流程
        if (appUser != null) {
            // 保存微信小程序授权信息
            saveWechatUser(appUser, wechatUser);
            return RestfulResult.success(buildUserInfo(appUser));
        }
        return RestfulResult.failure();
    }

    @Override
    public UserInfo buildUserInfo(AppUser appUser) {
        // 登录成功，构建用户返回信息
        UserInfo userInfo = new UserInfo();
        userInfo.transform(appUser);
        String token = appSecurityUtils.generateToken(appUser);
        userInfo.setToken(token);
        // 自定义token有效状态先于jwt10秒失效
        int expiredInterval = jwtConfiguration.getExpiredIntervalSeconds() - 10;
        userInfo.setTokenEffectiveTime(expiredInterval * Times.MS_ONE_SECOND);
        // 保存用户登录token
        saveUserToken(appUser, token, expiredInterval);
        return userInfo;
    }

    private void saveWechatUser(AppUser appUser, WechatUser wechatUser) {
        if (wechatUser != null) {
            // 登录成功检查更新微信用户授权信息
            AppUserWechat appUserWechat = new AppUserWechat();
            appUserWechat.setUserId(appUser.getId());
            appUserWechat.setOpenid(wechatUser.getOpenid());
            appUserWechat.setUnionId(wechatUser.getUnionId());
            appUserWechat.setSessionKey(wechatUser.getSessionKey());
            appUserWechat.setAppType(EnumValueHelper.getValue(WechatAppType.MP));
            LambdaQueryWrapper<AppUserWechat> wechatWrapper = Wrappers.lambdaQuery();
            wechatWrapper.eq(AppUserWechat::getUserId, appUser.getId());
            wechatWrapper.eq(AppUserWechat::getOpenid, appUser.getOpenid());
            if (appUserWechatService.count(wechatWrapper) > 0) {
                appUserWechatService.update(appUserWechat, wechatWrapper);
            } else {
                appUserWechatService.save(appUserWechat);
            }
        }
    }

    private void saveUserToken(AppUser appUser, String token, int expiredInterval) {
        // 用户操作凭证记录
        AppUserToken userToken = new AppUserToken();
        userToken.setUserId(appUser.getId());
        String identity = appUser.getUsername();
        userToken.setIdentity(identity);
        userToken.setToken(token);
        userToken.setExpiredInterval(expiredInterval);
        LocalDateTime currentDate = LocalDateTime.now();
        userToken.setCreateTime(currentDate);
        userToken.setUpdateTime(currentDate);
        userToken.setExpirationTime(TemporalUtil.addSeconds(currentDate, expiredInterval));
        userTokenService.save(userToken);
    }

    private AppUser addAppUser(AppLoginParam appLoginParam, String source) {
        AppUser appUserParam = new AppUser();
        String username = appLoginParam.getUsername();
        if (StringUtils.isEmpty(username)) { // 系统全局唯一的用户名为空则系统自动生成
            LambdaQueryWrapper<AppUser> queryWrapper = Wrappers.lambdaQuery();
            do { // 校验该用户名是否唯一，重复则循环获取
                username = StringUtil.randomNormalMixeds(9);
                queryWrapper.eq(AppUser::getUsername, username);
            } while (this.count(queryWrapper) > 0);
        }
        appUserParam.setUsername(username);
        appUserParam.setOpenid(appLoginParam.getOpenid());
        appUserParam.setAppid(appLoginParam.getAppid());
        appUserParam.setAvatar(appLoginParam.getAvatar());
        appUserParam.setNickname(appLoginParam.getNickname());
        String password = appLoginParam.getPassword();
        if (StringUtils.isBlank(password)) {
            password = ProjectConstants.DEFAULT_USER_PWD;
        }
        // 加盐密码
        String salt = AppPassUtils.getRandomSalt();
        appUserParam.setSalt(salt);
        String encodedPassword = AppPassUtils.encryptPassword(password, salt);
        appUserParam.setPassword(encodedPassword);
        appUserParam.setSource(source);
        appUserParam.setStatus(1);
        if (this.save(appUserParam)) {
            return this.getOne(Wrappers.lambdaQuery(appUserParam));
        }
        return null;
    }

    @Override
    public RestfulResult<?> authAppLogin(AppLoginParam appLoginParam, HttpServletRequest request) {
        // 登录方式判定
        String loginMode = appLoginParam.getLoginMode();
        String host = WebHttpUtil.getRemoteAddress(request);
        if (LoginModeEnum.ACCOUNT.getValue().equalsIgnoreCase(loginMode)) {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken();
            usernamePasswordToken.setRememberMe(true);
            usernamePasswordToken.setHost(host);
            usernamePasswordToken.setUsername(appLoginParam.getUsername());
            usernamePasswordToken.setPassword(appLoginParam.getPassword());
            return subjectLogin(usernamePasswordToken);
        } else if (LoginModeEnum.SMS.getValue().equalsIgnoreCase(loginMode)) {
            // 短信登录方式
            SmsVerifyToken smsVerifyToken = new SmsVerifyToken();
            smsVerifyToken.setRememberMe(true);
            smsVerifyToken.setHost(host);
        } else {
            RestfulResult<?> result = appLogin(appLoginParam, request);
            if (result.isSuccess()) { // 安全验证框架登录
                DefaultAuthToken authToken = new DefaultAuthToken();
                authToken.setRememberMe(true);
                authToken.setHost(host);
                authToken.setPrincipal(result.getData());
                return subjectLogin(authToken);
            }
            return RestfulResult.failure(result.getMessage());
        }
        return RestfulResult.failure();
    }

    private RestfulResult<?> subjectLogin(AuthenticationToken authToken) {
        try {
            SubjectUtils.getSubject().login(authToken);
            if (ObjectUtils.isNotEmpty(SubjectUtils.getHelperUser())) {
                return RestfulResult.success(SubjectUtils.getHelperUser().getUserInfo());
            } else {
                return RestfulResult.failure();
            }
        } catch (Exception e) {
//            LogUtil.error(getClass(), e);
            return RestfulResult.failure(e.getMessage());
        }
    }

    @Override
    public RestfulResult<?> loginVerify(HttpServletRequest request) {
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        if (StringUtils.isNotBlank(token)) {
            try {
                if (appSecurityUtils.tokenVerify(token)) {
                    return RestfulResult.success(this.getUserByToken(token));
                }
            } catch (Exception e) { // 验证过程中会抛出特定异常
                if (e instanceof TokenExpiredException) {
                    LogUtil.warn(getClass(), e.getMessage());
                    return RestfulResult.failure(ExceptionEnum.TOKEN_EXPIRED.getCode(), e.getMessage());
                }
            }
        }
        return RestfulResult.failure(AuthConstants.LOGGED_OUT, AuthConstants.LOGGED_OUT_REASON);
    }

    @Override
    public RestfulResult<?> appLogout(HttpServletRequest request) {
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        boolean tokenVerify = false;
        try {
            tokenVerify = appSecurityUtils.tokenVerify(token);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
        if (tokenVerify) {
            // 登出成功token失效处理
            LambdaQueryWrapper<AppUserToken> queryWrapper = Wrappers.lambdaQuery();
            UserIdentityToken userIdentityToken = appSecurityUtils.parseToken(token);
            queryWrapper.eq(AppUserToken::getUserId, userIdentityToken.getUserId());
            queryWrapper.eq(AppUserToken::getIdentity, userIdentityToken.getIdentity());
            queryWrapper.eq(AppUserToken::getToken, token);
            AppUserToken userToken = userTokenService.getOne(queryWrapper, false);
            if (userToken != null) {
                userToken.setStatus(4); // 登出状态
                userToken.setLogoutTime(LocalDateTime.now());
                userTokenService.updateById(userToken);
            }
            // 安全验证框架登出
            SubjectUtils.getSubject().logout();
            return RestfulResult.success();
        }
        return RestfulResult.failure();
    }

    @Override
    public UserInfo getUserByToken(String token) {
        HelperUser helperUser = SubjectUtils.getHelperUser();
        if (helperUser != null) {
            return helperUser.getUserInfo();
        }
        if (StringUtils.isNotBlank(token)) {
            UserIdentityToken userIdentityToken = appSecurityUtils.parseToken(token);
            AppUser appUser = this.getById(userIdentityToken.getUserId());
            if (appUser != null) {
                UserInfo userInfo = new UserInfo();
                userInfo.transform(appUser); // 用户详情类型转换
                userInfo.setToken(token);
                int expiredInterval = jwtConfiguration.getExpiredIntervalSeconds() - 10;
                userInfo.setTokenEffectiveTime(expiredInterval * Times.MS_ONE_SECOND);
                return userInfo;
            }
        }
        return null;
    }

    @Override
    public boolean updateUser(UpdateUserParam updateUserParam, HttpServletRequest request) {
        // 如果未传入更新的手机号则尝试从微信加密数据中获取
        if (StringUtils.isEmpty(updateUserParam.getPhone()) && updateUserParam.getEncryptedData() != null) {
            LambdaQueryWrapper<AppUserWechat> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(AppUserWechat::getUserId, updateUserParam.getUserId());
            AppUserWechat appUserWechat = appUserWechatService.getOne(queryWrapper, false);
            EncryptedData encryptedData = updateUserParam.getEncryptedData();
            Map<String, Object> dataRes = wechatMpManager.decryptData(encryptedData, appUserWechat.getSessionKey());
            if (dataRes == null) {
                return false;
            }
            updateUserParam.setPhone((String) dataRes.get("purePhoneNumber"));
        }
        AppUser appUser = new AppUser();
        appUser.setId(updateUserParam.getUserId());
        if (StringUtils.isNotEmpty(updateUserParam.getPhone())) {
            appUser.setPhone(updateUserParam.getPhone());
        }
        if (StringUtils.isNotEmpty(updateUserParam.getEmail())) {
            appUser.setEmail(updateUserParam.getEmail());
        }
        if (StringUtils.isNotEmpty(updateUserParam.getAvatar())) {
            appUser.setAvatar(updateUserParam.getAvatar());
        }
        if (StringUtils.isNotEmpty(updateUserParam.getNickname())) {
            appUser.setNickname(updateUserParam.getNickname());
        }
        return this.updateById(appUser);
    }
}
