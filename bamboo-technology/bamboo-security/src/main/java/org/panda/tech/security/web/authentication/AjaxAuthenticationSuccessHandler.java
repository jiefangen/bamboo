package org.panda.tech.security.web.authentication;

import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 支持AJAX请求的授权成功处理器
 */
public abstract class AjaxAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public final void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        if (WebHttpUtil.isAjaxRequest(request)) {
            Object result = getAjaxLoginResult(request, authentication);
            if (result != null) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().print(JsonUtil.toJson(result));
            }
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    protected abstract Object getAjaxLoginResult(HttpServletRequest request, Authentication authentication);

}
