package org.panda.service.auth.infrastructure.security.authentication;

import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.tech.security.user.UserSpecificDetails;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持多登录方式的Auth服务端认证提供者
 */
@Component
public class LoginModeAuthServerAuthenticationProvider
        extends AbstractAuthServerAuthenticationProvider<AbstractAuthenticationToken> implements ContextInitializedBean {

    private Map<Class<?>, AuthServerLoginAuthenticator<AbstractAuthenticationToken>> authenticators = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void afterInitialized(ApplicationContext context) throws Exception {
        context.getBeansOfType(AuthServerLoginAuthenticator.class).forEach((id, authenticator) -> {
            Class<?> tokenType = authenticator.getTokenType();
            assert tokenType != null;
            this.authenticators.put(tokenType, authenticator);
        });
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
                || this.authenticators.containsKey(authentication);
    }

    @Override
    protected UserSpecificDetails<?> authenticate(String appName, String scope, AbstractAuthenticationToken token) {
        AuthServerLoginAuthenticator<AbstractAuthenticationToken> authenticator = this.authenticators
                .get(token.getClass());
        if (authenticator != null) {
            return authenticator.authenticate(appName, scope, token);
        }
        return null;
    }

}
