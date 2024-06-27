package org.panda.business.helper.app.service.impl;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.bamboo.common.constant.basic.Times;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.business.helper.app.common.constant.AppSourceType;
import org.panda.business.helper.app.common.constant.ProjectConstants;
import org.panda.business.helper.app.infrastructure.security.AppSecurityUtil;
import org.panda.business.helper.app.infrastructure.security.user.UserIdentityToken;
import org.panda.business.helper.app.model.entity.AppUser;
import org.panda.business.helper.app.model.entity.AppUserToken;
import org.panda.business.helper.app.model.params.AppLoginParam;
import org.panda.business.helper.app.model.vo.UserInfo;
import org.panda.business.helper.app.repository.AppUserMapper;
import org.panda.business.helper.app.service.AppUserService;
import org.panda.business.helper.app.service.AppUserTokenService;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.exception.business.auth.AuthConstants;
import org.panda.tech.core.jwt.internal.InternalJwtConfiguration;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {

    @Autowired
    private AppSecurityUtil appSecurityUtil;
    @Autowired
    private InternalJwtConfiguration jwtConfiguration;
    @Autowired
    private AppUserTokenService userTokenService;

    @Transactional
    @Override
    public RestfulResult<?> appLogin(AppLoginParam appLoginParam, HttpServletRequest request) {
        // 获取app用户来源
        String appSource = appSecurityUtil.getSourceHeader(request);
        // TODO 接入微信小程序授权信息
        if (Objects.equals(EnumValueHelper.getValue(AppSourceType.WECHAT_MINI), appSource)) {
            String code = appLoginParam.getCode();
            // 利用临时code获取openid等微信授权信息
        }

        String username = appLoginParam.getUsername();
        // 判断登录账户是否存在
        LambdaQueryWrapper<AppUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AppUser::getUsername, username);
        queryWrapper.eq(AppUser::getAppid, appLoginParam.getAppid());
        queryWrapper.eq(AppUser::getSource, appSource);
        AppUser appUser = this.getOne(queryWrapper);
        if (appUser == null) { // 用户不存在即自动注册
            appUser = addAppUser(appLoginParam, appSource);
        }
        // 获取到用户后进入登录流程
        if (appUser != null) {
            // TODO 登录流程，接入shiro后实现

            UserInfo userInfo = new UserInfo();
            userInfo.transform(appUser);
            // 登录成功，生成用户toke返回，用于前后端交互凭证
            String token = appSecurityUtil.generateToken(appUser);
            userInfo.setToken(token);
            // 自定义token有效状态先于jwt10秒失效
            int expiredInterval = jwtConfiguration.getExpiredIntervalSeconds() - 10;
            userInfo.setTokenEffectiveTime(expiredInterval * Times.MS_ONE_SECOND);
            saveUserToken(appUser, token, expiredInterval);
            return RestfulResult.success(userInfo);
        }
        return  RestfulResult.failure();
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
        appUserParam.setAppid(appLoginParam.getAppid());
        appUserParam.setAvatar(appLoginParam.getAvatar());
        appUserParam.setNickname(appLoginParam.getNickname());
        String password = appLoginParam.getPassword();
        if (StringUtils.isBlank(password)) {
            password = ProjectConstants.DEFAULT_USER_PWD;
        }
        // TODO 加盐密码，接入shiro后实现
//        String salt = shiroEncrypt.getRandomSalt();
//        appUserParam.setSalt(salt);
//        String encodedPassword = shiroEncrypt.encryptPassword(password, salt);
//        appUserParam.setPassword(encodedPassword);
        appUserParam.setSource(source);
        appUserParam.setStatus(1);
        if (this.save(appUserParam)) {
            return this.getOne(Wrappers.lambdaQuery(appUserParam));
        }
        return null;
    }

    @Override
    public RestfulResult<?> loginVerify(HttpServletRequest request) {
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        if (StringUtils.isNotBlank(token)) {
            try {
                if (appSecurityUtil.tokenVerify(token)) {
                    // TODO 登录凭证认证鉴权验证，接入shiro后实现

                    return RestfulResult.success();
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
            tokenVerify = appSecurityUtil.tokenVerify(token);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
        if (tokenVerify) {
            // TODO 登出流程，接入shiro后实现

            // 登出成功token失效处理
            LambdaQueryWrapper<AppUserToken> queryWrapper = Wrappers.lambdaQuery();
            UserIdentityToken userIdentityToken = appSecurityUtil.parseToken(token);
            queryWrapper.eq(AppUserToken::getUserId, userIdentityToken.getUserId());
            queryWrapper.eq(AppUserToken::getIdentity, userIdentityToken.getIdentity());
            queryWrapper.eq(AppUserToken::getToken, token);
            AppUserToken userToken = userTokenService.getOne(queryWrapper, false);
            if (userToken != null) {
                userToken.setStatus(4); // 登出状态
                userToken.setLogoutTime(LocalDateTime.now());
                userTokenService.updateById(userToken);
            }
            return RestfulResult.success();
        }
        return RestfulResult.failure();
    }

    @Override
    public UserInfo getUserByToken(String token) {
        if (StringUtils.isNotBlank(token)) {
            UserIdentityToken userIdentityToken = appSecurityUtil.parseToken(token);
            AppUser appUser = this.getById(userIdentityToken.getUserId());
            if (appUser != null) {
                UserInfo userInfo = new UserInfo();
                userInfo.transform(appUser); // 用户详情类型转换
                return userInfo;
            }
        }
        return null;
    }
}
