package org.panda.service.auth.infrastructure.security.authentication.logout;

import org.panda.bamboo.common.util.LogUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Auth服务端登出处理器实现
 */
@Component
public class AuthServerLogoutHandlerImpl implements AuthServerLogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LogUtil.warn(getClass(), "AuthServerLogoutHandler...");
    }

}
