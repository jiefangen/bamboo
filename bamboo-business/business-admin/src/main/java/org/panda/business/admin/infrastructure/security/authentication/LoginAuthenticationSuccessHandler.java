package org.panda.business.admin.infrastructure.security.authentication;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.business.admin.modules.monitor.service.SysActionLogService;
import org.panda.business.admin.modules.monitor.service.SysUserTokenService;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.jwt.InternalJwtConfiguration;
import org.panda.tech.core.spec.user.DefaultUserIdentity;
import org.panda.tech.core.web.jwt.InternalJwtResolver;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.panda.tech.security.util.SecurityUtil;
import org.panda.tech.security.web.authentication.AjaxAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 配置登录认证成功的处理器
 *
 * @author fangen
 **/
@Component
public class LoginAuthenticationSuccessHandler extends AjaxAuthenticationSuccessHandler {

    @Autowired
    private InternalJwtResolver jwtResolver;
    @Autowired
    private InternalJwtConfiguration jwtConfiguration;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysActionLogService actionLogService;

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    @Override
    protected Object getAjaxLoginResult(HttpServletRequest request, Authentication authentication) {
        // 安全认证登录成功后的业务处理
        if (SecurityUtil.isAuthorized() && jwtResolver.isGenerable(appName) ) {
            DefaultUserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
            // 登录成功，生成用户toke返回，用于前后端交互凭证
            String token = jwtResolver.generate(appName, userSpecificDetails);

            // 用户交互凭证状态存储
            DefaultUserIdentity userIdentity = userSpecificDetails.getIdentity();
            SysUserToken userToken = new SysUserToken();
            userToken.setUserId(userIdentity.getId());
            String identity = userSpecificDetails.getUsername();
            userToken.setIdentity(identity);
            userToken.setToken(token);
            // 自定义token有效状态先于jwt10秒失效
            Integer expiredInterval = jwtConfiguration.getExpiredIntervalSeconds() - 10;
            userToken.setExpiredInterval(expiredInterval);
            LocalDateTime currentDate = LocalDateTime.now();
            userToken.setCreateTime(currentDate);
            userToken.setUpdateTime(currentDate);
            userToken.setExpirationTime(TemporalUtil.addSeconds(currentDate, expiredInterval));
            sysUserTokenService.save(userToken);

            LambdaQueryWrapper<SysUserToken> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUserToken::getToken, token);
            SysUserToken sysUserToken = sysUserTokenService.getOne(queryWrapper, false);
            // 登录日志记录
            actionLogService.intoLoginLog(request, sysUserToken);
            return RestfulResult.success(token);
        }
        return RestfulResult.failure();
    }
}
