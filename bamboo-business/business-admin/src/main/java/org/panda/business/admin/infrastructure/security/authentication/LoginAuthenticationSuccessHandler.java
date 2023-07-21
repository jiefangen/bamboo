package org.panda.business.admin.infrastructure.security.authentication;

import cn.hutool.http.useragent.UserAgent;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.business.admin.modules.monitor.service.SysUserTokenService;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.jwt.InternalJwtConfiguration;
import org.panda.tech.core.spec.user.DefaultUserIdentity;
import org.panda.tech.core.web.jwt.InternalJwtResolver;
import org.panda.tech.core.web.model.IPAddress;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
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
            userToken.setIdentity(userSpecificDetails.getUsername());
            userToken.setToken(token);
            Integer expiredInterval = jwtConfiguration.getExpiredIntervalSeconds();
            userToken.setExpiredInterval(expiredInterval);
            LocalDateTime currentDate = LocalDateTime.now();
            userToken.setCreateTime(currentDate);
            userToken.setExpirationTime(TemporalUtil.addSeconds(currentDate, expiredInterval));
            sysUserTokenService.save(userToken);
            // 日志记录 查询条件：登录地址、用户名；搜索/重置
            // Token凭证、登录身份、主机IP、登录地址、浏览器、操作系统、登录时间
            // 功能：强制登出
            String remoteAddress = WebHttpUtil.getRemoteAddress(request);
            if (!NetUtil.isIntranetIp(remoteAddress)) {
                IPAddress ipAddress = WebHttpUtil.getIPAddress(remoteAddress, Strings.LOCALE_SC);
                String ipAttribution = ipAddress.getRegion() + Strings.SPACE + ipAddress.getCity();
            } else {
                String ipAttribution = "内网IP";
            }
            UserAgent userAgent = WebHttpUtil.getUserAgent(request);
            if (userAgent != null) {
                String browser = userAgent.getBrowser().getName();
                String os = userAgent.getOs().getName();
            }
            return RestfulResult.success(token);
        }
        return RestfulResult.failure();
    }
}
