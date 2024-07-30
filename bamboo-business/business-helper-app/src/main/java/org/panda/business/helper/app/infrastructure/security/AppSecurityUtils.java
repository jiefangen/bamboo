package org.panda.business.helper.app.infrastructure.security;

import org.panda.business.helper.app.model.entity.AppUser;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.jwt.internal.resolver.InternalJwtResolver;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * APP安全工具
 *
 * @author fangen
 * @since JDK 11 2024/6/11
 **/
@Component
public class AppSecurityUtils {

    @Autowired
    private InternalJwtResolver jwtResolver;

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    public String generateToken(AppUser appUser) {
        UserIdentityToken userIdentityToken = new UserIdentityToken();
        userIdentityToken.setUserId(appUser.getId());
        userIdentityToken.setCredentials(appUser.getPassword());
        userIdentityToken.setIdentity(appUser.getUsername());
        // 生成用户toke返回，用于前后端交互凭证
        return jwtResolver.generate(appName, userIdentityToken);
    }

    public UserIdentityToken parseToken(String token) {
        return this.jwtResolver.parse(token, UserIdentityToken.class);
    }

    public boolean tokenVerify(String token) {
        return this.jwtResolver.verify(token);
    }

    /**
     * 获取请求来源
     */
    public String getSourceHeader(HttpServletRequest request) {
        return WebHttpUtil.getHeader(request, WebConstants.HEADER_SOURCE);
    }
}
