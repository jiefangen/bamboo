package org.panda.business.helper.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.panda.business.helper.app.common.constant.ProjectConstants;
import org.panda.business.helper.app.model.entity.AppUser;
import org.panda.business.helper.app.model.params.AppLoginParam;
import org.panda.business.helper.app.repository.AppUserMapper;
import org.panda.business.helper.app.service.AppUserService;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.jwt.internal.resolver.InternalJwtResolver;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private InternalJwtResolver jwtResolver;
    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    @Override
    public RestfulResult<?> appLogin(AppLoginParam appLoginParam) {
        String username = appLoginParam.getUsername();
        // 判断登录账户是否存在
        LambdaQueryWrapper<AppUser> queryWrapper = Wrappers.lambdaQuery();;
        queryWrapper.eq(AppUser::getUsername, username);
        if (StringUtils.isNotBlank(appLoginParam.getOpenid())) {
            queryWrapper.eq(AppUser::getOpenid, appLoginParam.getOpenid());
        }
        AppUser appUser = this.getOne(queryWrapper);
        if (appUser == null) { // 注册
            appUser = addAppUser(appLoginParam);
        }
        if (appUser != null) {
            return RestfulResult.failure();
        }

        // 登录流程
//        Subject subject = SecurityUtils.getSubject();
//        String password = appLoginParam.getPassword();
//        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
//        try {
//            subject.login(usernamePasswordToken);
//            // 登录成功，生成用户toke返回，用于前后端交互凭证
//            String token = jwtResolver.generate(appName, appLoginParam.getUsername());
//            Map<String, String> loginRes = new HashMap<>();
//            loginRes.put("name", username);
//            loginRes.put("token", token);
//            return RestfulResult.success(loginRes);
//        } catch (UnknownAccountException e) {
//            return RestfulResult.failure(ProjectConstants.USER_INFO_ERROR, e.getMessage());
//        } catch (IncorrectCredentialsException e) {
//            LogUtil.warn(getClass(), ProjectConstants.PWD_WRONG);
//            return RestfulResult.failure(ProjectConstants.USER_INFO_ERROR, ProjectConstants.PWD_WRONG);
//        } catch (AccountException e) {
//            return RestfulResult.failure(ProjectConstants.USER_INFO_ERROR, e.getMessage());
//        }
        return null;
    }

    private AppUser addAppUser(AppLoginParam appLoginParam) {
        AppUser appUserParam = new AppUser();
        appUserParam.setUsername(appLoginParam.getUsername());
        appUserParam.setOpenid(appLoginParam.getOpenid());
        appUserParam.setAvatar(appLoginParam.getAvatar());
//        String salt = shiroEncrypt.getRandomSalt();
//        appUserParam.setSalt(salt);
        String password = appLoginParam.getPassword();
        if (StringUtils.isBlank(password)) {
            password = ProjectConstants.DEFAULT_USER_PWD;
        }
//        String encodedPassword = shiroEncrypt.encryptPassword(password, salt);
//        appUserParam.setPassword(encodedPassword);
        appUserParam.setStatus(1);
        if (this.save(appUserParam)) {
            return this.getOne(Wrappers.lambdaQuery(appUserParam));
        }
        return null;
    }
}
