package org.panda.business.official.infrastructure.security.authentication;

import org.panda.tech.core.config.app.AppConstants;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 配置认证成功的jwt处理器
 *
 * @author fangen
 **/
@Component
public class JwtAuthenticationSuccessHandler extends AjaxAuthenticationSuccessHandler {

    @Autowired
    private InternalJwtResolver jwtResolver;

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    @Override
    protected Object getAjaxLoginResult(HttpServletRequest request, Authentication authentication) {
        // 安全认证登录成功后的业务处理
        DefaultUserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        // 登录成功，生成用户toke返回，用于前后端交互凭证
        String token = jwtResolver.generate(appName, userSpecificDetails);
        Map<String, String> userMap = new HashMap<>(3);
        userMap.put("username", userSpecificDetails.getUsername());
        userMap.put("token", token);
        return RestfulResult.success(userMap);
    }
}
