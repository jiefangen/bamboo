package org.panda.service.auth.infrastructure.security.authentication.logout;

import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.core.webmvc.jwt.JwtParser;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Auth服务端登出成功处理器
 */
public class AuthServerLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private JwtParser jwtParser;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String type = request.getHeader(WebConstants.HEADER_AUTH_TYPE);
        String jwt = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        DefaultUserSpecificDetails userSpecificDetails = jwtParser.parse(type, jwt, DefaultUserSpecificDetails.class);
        if (userSpecificDetails != null) {
            // 登出成功token失效处理
            // 处理完成返回登出成功
            WebHttpUtil.buildJsonResponse(response, RestfulResult.success());
            return;
        }
        super.handle(request, response, authentication);
    }

}
