package org.panda.business.helper.app.infrastructure.security;

import org.panda.business.helper.app.common.constant.ProjectConstants;
import org.panda.business.helper.app.infrastructure.security.user.UserIdentityToken;
import org.panda.business.helper.app.model.entity.AppUser;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.jwt.internal.resolver.InternalJwtResolver;
import org.panda.tech.core.web.context.SpringWebContext;
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
public class AppSecurityUtil {

    @Autowired
    private InternalJwtResolver jwtResolver;

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    public String generateToken(AppUser appUser) {
        UserIdentityToken userIdentityToken = new UserIdentityToken();
        userIdentityToken.setUserId(appUser.getId());
        userIdentityToken.setIdentity(appUser.getUsername());
        userIdentityToken.setPassword(appUser.getPassword());
        userIdentityToken.setUserRank(appUser.getUserRank());
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
    public String getSourceHeader() {
        HttpServletRequest request = SpringWebContext.getRequest();
        return WebHttpUtil.getHeader(request, ProjectConstants.HEADER_SOURCE);
    }
}
