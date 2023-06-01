package org.panda.tech.security.authentication;

import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持登录服务端认证提供者
 */
@Component
public class LoginModeAuthenticationProvider
        extends AbstractAuthenticationProvider<AbstractAuthenticationToken> implements ContextInitializedBean {

    private Map<Class<?>, LoginAuthenticator<AbstractAuthenticationToken>> authenticators = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void afterInitialized(ApplicationContext context) throws Exception {
        context.getBeansOfType(LoginAuthenticator.class).forEach((id, authenticator) -> {
            Class<?> tokenType = authenticator.getTokenType();
            assert tokenType != null;
            this.authenticators.put(tokenType, authenticator);
        });
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
                || this.authenticators.containsKey(authentication);
    }

}
