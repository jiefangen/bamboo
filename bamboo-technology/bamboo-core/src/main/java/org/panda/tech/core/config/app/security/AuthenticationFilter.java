package org.panda.tech.core.config.app.security;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 抽象认证鉴权过滤器
 *
 * @author fangen
 **/
public class AuthenticationFilter extends AbstractAuthSupport implements Filter {

    private final Logger LOGGER = LogUtil.getLogger(getClass());

    private final Class<?> strategyType;

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String server;

    public AuthenticationFilter(Class<?> strategyType) {
        this.strategyType = strategyType;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String token = WebHttpUtil.getHeader(request, WebConstants.HEADER_AUTH_JWT);
        if (StringUtils.isBlank(token)) {
            String uri = WebHttpUtil.getRelativeRequestUrl(request);
            LOGGER.debug("Automatic authentication uri: {}", uri);
            String credentials = WebHttpUtil.getHeader(request, WebConstants.HEADER_AUTH_CREDENTIALS);
            if (StringUtils.isNotBlank(credentials)) { // 触发服务自动认证获取交互凭证
                try {
                    String secretKey = WebHttpUtil.getHeader(request, WebConstants.HEADER_SECRET_KEY);
                    // 账户凭证验证签名请求凭证
                    RestfulResult<String> tokenResult = super.getAuthStrategy().getTokenByCredentials(secretKey,
                            credentials, server);
                    if (Commons.RESULT_SUCCESS.equals(tokenResult.getMessage())) {
                        request.setAttribute(WebConstants.HEADER_AUTH_JWT, tokenResult.getData());
                    } else {
                        WebHttpUtil.buildJsonResponse((HttpServletResponse) resp, tokenResult);
                        return;
                    }
                } catch (Exception e) {
                    LogUtil.error(getClass(), e);
                    WebHttpUtil.buildJsonResponse((HttpServletResponse) resp, e.getMessage());
                    return;
                }
            }
        }
        chain.doFilter(req, resp);
    }

    @Override
    protected Class<?> getStrategyType() {
        return this.strategyType;
    }
}
