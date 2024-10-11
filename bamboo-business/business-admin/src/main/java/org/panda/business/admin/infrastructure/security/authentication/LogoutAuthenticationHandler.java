package org.panda.business.admin.infrastructure.security.authentication;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.admin.modules.monitor.service.SysUserTokenService;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import org.panda.tech.core.jwt.internal.resolver.InternalJwtResolver;
import org.panda.tech.core.web.config.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 服务端认证登出处理器
 **/
@Component
public class LogoutAuthenticationHandler {

    @Autowired
    private InternalJwtResolver jwtResolver;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    public void onLogout(HttpServletRequest request) {
        String jwt = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        boolean jwtVerify = false;
        try {
            jwtVerify = jwtResolver.verify(jwt);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
        if (jwtVerify) {
            // 登出成功token失效处理
            LambdaQueryWrapper<SysUserToken> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(SysUserToken::getToken, jwt);
            SysUserToken userToken = sysUserTokenService.getOne(queryWrapper, false);
            if (userToken != null) {
                userToken.setStatus(4); // 登出状态
                userToken.setLogoutTime(LocalDateTime.now());
                sysUserTokenService.updateById(userToken);
            }
        }
    }

}
