package org.panda.tech.security.web.authentication;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 基于可解决异常的登录认证失败处理器
 */
public abstract class ResolvableExceptionAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        Object result = getFailureResult(request, exception);
        if (result != null) {
            response.getWriter().print(JsonUtil.toJson(result));
        } else {
            LogUtil.error(getClass(), exception); // 未知登录认证异常打印，方便后续追溯
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(JsonUtil.toJson(RestfulResult.getFailure(ExceptionEnum.UNAUTHORIZED)));
        }
    }

    protected abstract Object getFailureResult(HttpServletRequest request, AuthenticationException exception);

}
