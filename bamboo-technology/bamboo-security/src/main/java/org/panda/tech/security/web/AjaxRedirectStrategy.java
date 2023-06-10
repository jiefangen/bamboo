package org.panda.tech.security.web;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.config.CommonProperties;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.mvc.cors.CorsRegistryProperties;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * AJAX特殊处理的重定向策略
 */
@Component
public class AjaxRedirectStrategy extends DefaultRedirectStrategy {

    private Set<String> allowedHostList = new HashSet<>();

    @Autowired(required = false)
    public void setCommonProperties(CommonProperties commonProperties) {
        this.allowedHostList.addAll(commonProperties.getAllAppUris());
    }

    @Autowired(required = false)
    public void setCorsRegistryProperties(CorsRegistryProperties corsRegistryProperties) {
        addAllowedHost(corsRegistryProperties.getAllowedOrigins());
    }

    public void addAllowedHost(String... allowedHost) {
        Collections.addAll(this.allowedHostList, allowedHost);
    }

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        String contextPath = request.getContextPath();
        if (Strings.SLASH.equals(contextPath)) {
            contextPath = Strings.EMPTY;
        }
        String redirectUrl = calculateRedirectUrl(contextPath, url);
        if (!isValidRedirectUrl(request, redirectUrl)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "illegal redirect target url: " + redirectUrl);
            return;
        }
        redirectUrl = response.encodeRedirectURL(redirectUrl);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Redirecting to '" + redirectUrl + "'");
        }

        if (WebHttpUtil.isAjaxRequest(request)) {
            // 去掉contextPath前缀
            if (contextPath.length() > 0 && redirectUrl.startsWith(contextPath)) {
                redirectUrl = redirectUrl.substring(contextPath.length());
            }
            // ajax重定向时，js端自动跳转不会带上origin头信息，导致目标站点cors校验失败。
            // 不得已只能将目标地址放到头信息中传递给js端，由js执行跳转以带上origin头信息，使得目标站点cors校验通过。
            // 成功和失败的请求都可能产生重定向动作，所以此处不设置响应状态码
            response.setHeader(WebConstants.HEADER_REDIRECT_TO, redirectUrl);
            String body = buildRedirectBody(redirectUrl);
            if (body != null) {
                response.getWriter().print(body);
            }
        } else {
            response.sendRedirect(redirectUrl);
        }
    }

    private boolean isValidRedirectUrl(HttpServletRequest request, String redirectUrl) {
        // 空地址无效
        if (StringUtils.isBlank(redirectUrl)) {
            return false;
        }
        // 相对路径地址可以重定向
        if (NetUtil.isRelativeUrl(redirectUrl)) {
            return true;
        }
        String redirectHost = NetUtil.getHost(redirectUrl, false);
        // 内网地址可以重定向，即使网段可能不同
        if (NetUtil.isLocalHost(redirectHost) || NetUtil.isIntranetIp(redirectHost)) {
            return true;
        }

        String requestHost = WebHttpUtil.getHost(request, false);
        // 同一个主机地址可以重定向，即使端口可能不同
        if (Objects.equals(requestHost, redirectHost)) {
            return true;
        }
        // 同一个顶级域名可以重定向
        String requestDomain = NetUtil.getTopDomain(requestHost);
        String redirectDomain = NetUtil.getTopDomain(redirectHost);
        if (Objects.equals(requestDomain, redirectDomain)) {
            return true;
        }
        // 匹配允许名单可以重定向
        return this.allowedHostList.contains(redirectHost);
    }

    protected String buildRedirectBody(String redirectUrl) {
        return "It should be redirected to " + redirectUrl;
    }

}
