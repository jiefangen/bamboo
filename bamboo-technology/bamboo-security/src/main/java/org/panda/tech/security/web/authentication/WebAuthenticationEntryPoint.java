package org.panda.tech.security.web.authentication;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.exception.ExceptionEnum;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.config.meta.ApiMetaProperties;
import org.panda.tech.core.web.mvc.util.WebMvcUtil;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.security.web.SecurityUrlProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * WEB未登录访问限制的进入点
 */
public class WebAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private SecurityUrlProvider securityUrlProvider;
    @Autowired
    private RedirectStrategy redirectStrategy;
    @Autowired
    private ApiMetaProperties apiMetaProperties;

    public WebAuthenticationEntryPoint(SecurityUrlProvider securityUrlProvider) {
        super(securityUrlProvider.getDefaultLoginUrl());
        this.securityUrlProvider = securityUrlProvider;
    }

    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) {
        String loginFormUrl = this.securityUrlProvider.getLoginUrl(request);

        String nextUrl;
        if (WebHttpUtil.isAjaxRequest(request)) {
            nextUrl = request.getHeader(WebConstants.HEADER_ORIGINAL_PAGE);
            if (StringUtils.isBlank(nextUrl)) {
                nextUrl = request.getHeader(WebConstants.HEADER_REFERER);
            }
        } else {
            nextUrl = request.getRequestURI();
            String queryString = request.getQueryString();
            if (StringUtils.isNotBlank(queryString)) {
                nextUrl += Strings.QUESTION + queryString;
            }
        }

        if (StringUtils.isNotBlank(nextUrl)) {
            String redirectParameter = this.apiMetaProperties.getRedirectTargetUrlParameter();
            loginFormUrl += Strings.AND + redirectParameter + Strings.EQUAL
                    + URLEncoder.encode(nextUrl, StandardCharsets.UTF_8);
        }

        return loginFormUrl;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        if (WebHttpUtil.isAjaxRequest(request)) { // AJAX请求执行特殊的跳转
            String loginPageUrl = buildRedirectUrlToLoginPage(request, response, authException);
            // AJAX POST请求无法通过自动登录重新提交，或者默认登录页面地址是相对地址（在当前应用，无需转发试探），则直接跳转到登录页面
            if (HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())
                    || NetUtil.isRelativeUrl(getLoginFormUrl())) {
                response.setHeader(WebConstants.HEADER_LOGIN_URL, loginPageUrl);
            } else {
                this.redirectStrategy.sendRedirect(request, response, loginPageUrl);
            }
            Object obj = RestfulResult.failure(ExceptionEnum.ILLEGAL_TOKEN.getCode(), ExceptionEnum.ILLEGAL_TOKEN.getMessage());
            WebHttpUtil.buildJsonResponse(response, obj);
            return;
        }
        if (WebMvcUtil.isInternalReq(request)) { // 内部RPC调用直接返回401错误
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Object obj = RestfulResult.failure(ExceptionEnum.ILLEGAL_TOKEN.getCode(), ExceptionEnum.ILLEGAL_TOKEN.getMessage());
            WebHttpUtil.buildJsonResponse(response, obj);
            return;
        }
        super.commence(request, response, authException);
    }

}
