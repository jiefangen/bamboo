package org.panda.service.auth.infrastructure.security.authentication;

import org.apache.commons.lang3.StringUtils;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.security.config.constants.SecurityConstants;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * AuthServiceAuthenticationDetails源
 */
public class AuthServiceAuthenticationDetailsSource implements
        AuthenticationDetailsSource<HttpServletRequest, AuthServiceAuthenticationDetails> {

    @Override
    public AuthServiceAuthenticationDetails buildDetails(HttpServletRequest request) {
        String service = WebHttpUtil.getParameterOrAttribute(request, SecurityConstants.PARAMETER_SERVICE);
        if (StringUtils.isBlank(service)) { // 为空则尝试从header中获取
            service = WebHttpUtil.getHeader(request, SecurityConstants.PARAMETER_SERVICE);
        }
        String scope = WebHttpUtil.getParameterOrAttribute(request, SecurityConstants.PARAMETER_SCOPE);
        if (StringUtils.isBlank(scope)) {
            scope = WebHttpUtil.getHeader(request, SecurityConstants.PARAMETER_SCOPE);
        }
        String ip = WebHttpUtil.getRemoteAddress(request);
        return new AuthServiceAuthenticationDetails(service, scope, ip);
    }

}
