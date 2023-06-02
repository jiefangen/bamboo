package org.panda.business.official.infrastructure.security.authentication;

import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.tech.security.authentication.AbstractAuthenticationProvider;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.panda.tech.security.user.UserSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持登录服务端认证提供者
 */
@Component
public class LoginAuthenticationProvider
        extends AbstractAuthenticationProvider<AbstractAuthenticationToken> implements ContextInitializedBean {

    private Map<Class<?>, LoginAuthenticator<AbstractAuthenticationToken>> authenticators = new HashMap<>();

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserSpecificDetailsService userDetailsService;

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
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        DefaultUserSpecificDetails userSpecificDetails = (DefaultUserSpecificDetails) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userSpecificDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userSpecificDetails, password, userSpecificDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
                || this.authenticators.containsKey(authentication);
    }

}
