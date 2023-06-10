package org.panda.tech.security.web.authentication;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.tech.core.web.config.LoginModeEnum;
import org.panda.tech.security.config.exception.BusinessAuthenticationException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 支持多种登录方式的认证过滤器。
 */
// 多种登录方式用一个过滤器处理的好处是登录处理URL可以与登录表单URL保持一致，从而有利于登录失败后的处理。
public class LoginModeAuthenticationFilter extends LoginAuthenticationFilter {

    public static final String PARAMETER_LOGIN_MODE = "loginMode";

    private AuthenticationTokenResolver<AbstractAuthenticationToken> defaultTokenResolver;
    private Map<String, AuthenticationTokenResolver<AbstractAuthenticationToken>> tokenResolverMapping = new HashMap<>();

    @SuppressWarnings("unchecked")
    public LoginModeAuthenticationFilter(ApplicationContext context) {
        super(context);

        this.tokenResolverMapping.clear();
        context.getBeansOfType(AuthenticationTokenResolver.class).forEach((id, resolver) -> {
            String loginMode = resolver.getLoginMode();
            if (loginMode == null) {
                this.defaultTokenResolver = resolver;
            } else {
                this.tokenResolverMapping.put(loginMode, resolver);
            }
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String loginMode = obtainLoginMode(request);
        AuthenticationTokenResolver<AbstractAuthenticationToken> resolver;
        if (loginMode == null) {
            resolver = this.defaultTokenResolver;
        } else {
            resolver = this.tokenResolverMapping.get(loginMode);
        }
        if (resolver != null) {
            try {
                AbstractAuthenticationToken authRequest = resolver.resolveAuthenticationToken(request);
                setDetails(request, authRequest);
                return getAuthenticationManager().authenticate(authRequest);
            } catch (BusinessException e) {
                throw new BusinessAuthenticationException(e);
            }
        }
        // 找不到匹配登录方式的构建器，则采用父类的用户名密码登录方式
        return super.attemptAuthentication(request, response);
    }

    public String obtainLoginMode(HttpServletRequest request) {
        String loginMode = request.getParameter(PARAMETER_LOGIN_MODE);
        if (StringUtils.isEmpty(loginMode)) { // 默认账户密码登录方式
            loginMode = LoginModeEnum.ACCOUNT.getValue();
        }
        return loginMode;
    }

    protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }


}
