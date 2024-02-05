package org.panda.service.auth.infrastructure.security.authentication;

import org.panda.tech.core.exception.business.BusinessException;
import org.panda.service.auth.infrastructure.security.service.AuthServerExceptionCodes;
import org.panda.service.auth.infrastructure.security.service.AuthServiceManager;
import org.panda.tech.security.authentication.AbstractAuthenticationProvider;
import org.panda.tech.security.user.UserSpecificDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 抽象的Auth服务端认证提供者
 *
 * @param <A> 认证类型
 */
public abstract class AbstractAuthServerAuthenticationProvider<A extends Authentication>
        extends AbstractAuthenticationProvider<A> {

    @Autowired
    private AuthServiceManager serviceManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        @SuppressWarnings("unchecked")
        A token = (A) authentication;
        Object details = token.getDetails();
        if (details instanceof AuthServiceAuthenticationDetails) {
            AuthServiceAuthenticationDetails authenticationDetails = (AuthServiceAuthenticationDetails) details;
            String service = authenticationDetails.getService();
            String appName = this.serviceManager.getAppName(service);
            if (appName == null) {
                throw new BusinessException(AuthServerExceptionCodes.INVALID_SERVICE);
            }
            String scope = authenticationDetails.getScope();
            String ip = authenticationDetails.getIp();
            try {
                UserSpecificDetails<?> userDetails = authenticate(appName, scope, token);
                if (userDetails == null) {
                    throw new BusinessException(AuthServerExceptionCodes.UNSUPPORTED_AUTHENTICATE_MODE);
                }
                return new AuthAccountSpecificDetailsAuthenticationToken(userDetails, service, scope, ip);
            } catch (BusinessException e) {
                throw new BadCredentialsException(e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

    protected abstract UserSpecificDetails<?> authenticate(String appName, String scope, A token);
}
