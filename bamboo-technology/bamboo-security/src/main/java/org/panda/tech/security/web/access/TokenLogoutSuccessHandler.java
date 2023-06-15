package org.panda.tech.security.web.access;

import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.jwt.InternalJwtResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token交互方式登出处理器
 **/
public class TokenLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private InternalJwtResolver jwtResolver;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String jwt = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        if (jwtResolver.verify(jwt)) {
            // 登出成功token失效处理，例如将JWT令牌添加到黑名单中
        }
        super.handle(request, response, authentication);
    }

}
