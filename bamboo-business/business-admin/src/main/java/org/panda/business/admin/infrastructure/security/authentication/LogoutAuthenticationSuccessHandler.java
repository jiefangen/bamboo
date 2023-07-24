package org.panda.business.admin.infrastructure.security.authentication;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.admin.modules.monitor.service.SysUserTokenService;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import org.panda.tech.core.spec.user.DefaultUserIdentity;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.jwt.InternalJwtResolver;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 配置登出成功处理器
 **/
@Component("simpleUrlLogoutSuccessHandler")
public class LogoutAuthenticationSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private InternalJwtResolver jwtResolver;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String jwt = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        boolean jwtVerify = false;
        try {
            jwtVerify = jwtResolver.verify(jwt);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
        if (jwtVerify) {
            // 登出成功token失效处理
            LambdaQueryWrapper<SysUserToken> queryWrapper = new LambdaQueryWrapper<>();
            DefaultUserSpecificDetails userSpecificDetails = jwtResolver.parse(jwt, DefaultUserSpecificDetails.class);
            DefaultUserIdentity userIdentity = userSpecificDetails.getIdentity();
            queryWrapper.eq(SysUserToken::getUserId, userIdentity.getId());
            queryWrapper.eq(SysUserToken::getIdentity, userSpecificDetails.getUsername());
            queryWrapper.eq(SysUserToken::getToken, jwt);
            SysUserToken userToken = sysUserTokenService.getOne(queryWrapper, false);
            if (userToken != null) {
                userToken.setStatus(4); // 登出状态
                userToken.setLogoutTime(LocalDateTime.now());
                sysUserTokenService.updateById(userToken);
            }
            // 处理完成返回登出成功
            WebHttpUtil.buildJsonResponse(response, RestfulResult.success());
            return;
        }
        super.handle(request, response, authentication);
    }

}
