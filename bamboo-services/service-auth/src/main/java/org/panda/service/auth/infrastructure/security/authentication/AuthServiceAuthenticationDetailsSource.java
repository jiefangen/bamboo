package org.panda.service.auth.infrastructure.security.authentication;

import org.apache.commons.lang3.StringUtils;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.security.cas.CasConstants;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * AuthServiceAuthenticationDetails源
 */
public class AuthServiceAuthenticationDetailsSource implements
        AuthenticationDetailsSource<HttpServletRequest, AuthServiceAuthenticationDetails> {

    @Override
    public AuthServiceAuthenticationDetails buildDetails(HttpServletRequest request) {
        String service = WebHttpUtil.getParameterOrAttribute(request, CasConstants.PARAMETER_SERVICE);
        if (StringUtils.isBlank(service)) { // 为空则尝试从header中获取
            service = WebHttpUtil.getHeader(request, CasConstants.PARAMETER_SERVICE);
        }
        String scope = WebHttpUtil.getParameterOrAttribute(request, CasConstants.PARAMETER_SCOPE);
        if (StringUtils.isBlank(scope)) {
            scope = WebHttpUtil.getHeader(request, CasConstants.PARAMETER_SCOPE);
        }
        String ip = WebHttpUtil.getRemoteAddress(request);
        return new AuthServiceAuthenticationDetails(service, scope, ip);
    }

}
