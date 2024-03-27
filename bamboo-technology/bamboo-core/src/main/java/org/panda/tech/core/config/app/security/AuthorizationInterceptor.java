package org.panda.tech.core.config.app.security;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.config.security.WebSecurityProperties;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 抽象认证鉴权拦截器
 *
 * @author fangen
 **/
public class AuthorizationInterceptor extends AbstractAuthSupport implements HandlerInterceptor {

    private final Class<?> strategyType;

    @Autowired
    private WebSecurityProperties securityProperties;

    public AuthorizationInterceptor(Class<?> strategyType) {
        this.strategyType = strategyType;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 请求资源预检查
        if (preCheck(request, response)) {
            return true;
        }
        // 令牌权限校验
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        if (StringUtils.isBlank(token)) {
            token = WebHttpUtil.getParameterOrAttribute(request, WebConstants.HEADER_AUTH_JWT);
        }
        if (StringUtils.isNotBlank(token)) {
            // Auth服务器验证令牌
            String api = WebHttpUtil.getRelativeRequestUrl(request);
            try {
                return super.getAuthStrategy().verification(token, api);
            } catch (Exception e) {
                LogUtil.error(getClass(), e);
                WebHttpUtil.buildJsonResponse(response, e.getMessage());
            }
        } else {
            Object obj = RestfulResult.getFailure(ExceptionEnum.UNAUTHORIZED);
            WebHttpUtil.buildJsonResponse(response, obj);
        }
        return false;
    }

    private boolean preCheck(HttpServletRequest request, HttpServletResponse response) {
        // 验证策略未配置则通过所有请求
        if (super.isNotAuthStrategy()) {
            return true;
        }
        // 预检请求直接通过
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return true;
        }
        // 忽略资源直接放行
        if (this.securityProperties != null) {
            List<String> ignoringPatterns = this.securityProperties.getIgnoringPatterns();
            if (ignoringPatterns != null) {
                String url = WebHttpUtil.getRelativeRequestUrl(request);
                for (String ignoringPattern : ignoringPatterns) {
                    if (StringUtil.antPathMatchOneOf(url, ignoringPattern)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected Class<?> getStrategyType() {
        return this.strategyType;
    }
}
