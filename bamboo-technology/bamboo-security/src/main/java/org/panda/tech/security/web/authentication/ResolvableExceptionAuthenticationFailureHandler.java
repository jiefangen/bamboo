package org.panda.tech.security.web.authentication;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.exception.ExceptionEnum;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 基于可解决异常的登录认证失败处理器
 */
public abstract class ResolvableExceptionAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired(required = false)
    private AuthenticationFailureViewResolver viewResolver = new DefaultAuthenticationFailureViewResolver();
    @Autowired
    private WebMvcProperties webMvcProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        // AJAX请求登录认证失败直接报401错误，不使用sendError()方法，以避免错误消息丢失
        if (WebHttpUtil.isAjaxRequest(request)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            Object result = getFailureResult(request, exception);
            if (result != null) {
                response.getWriter().print(JsonUtil.toJson(result));
            } else {
                LogUtil.error(getClass(), exception); // 未知登录认证异常打印，方便后续追溯
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().print(JsonUtil.toJson(
                        RestfulResult.failure(ExceptionEnum.UNAUTHORIZED.getCode(), ExceptionEnum.UNAUTHORIZED.getMessage())));
            }
        } else {
            String targetView = this.viewResolver.resolveFailureView(request);
            if (StringUtils.isBlank(targetView)) { // 登录认证失败后的目标地址未设置，则报401错误
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else { // 跳转到目标地址
                boolean useForward = true;
                String targetUrl = targetView;
                if (targetUrl.startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX)) {
                    targetUrl = targetUrl.substring(UrlBasedViewResolver.REDIRECT_URL_PREFIX.length());
                    useForward = false;
                }
                // 不是http地址又不是相对地址，则前面加/以视为相对地址
                if (!NetUtil.isHttpUrl(targetUrl, true) && !NetUtil.isRelativeUrl(targetUrl)) {
                    targetUrl = Strings.SLASH + targetUrl;
                }
                if (useForward) {
                    WebMvcProperties.View view = this.webMvcProperties.getView();
                    targetUrl = view.getPrefix() + targetUrl + view.getSuffix();
                    targetUrl = targetUrl.replaceAll(Strings.DOUBLE_SLASH, Strings.SLASH);
                    request.getRequestDispatcher(targetUrl).forward(request, response);
                } else {
                    response.sendRedirect(targetUrl);
                }
            }
        }
    }

    protected abstract Object getFailureResult(HttpServletRequest request, AuthenticationException exception);

}
