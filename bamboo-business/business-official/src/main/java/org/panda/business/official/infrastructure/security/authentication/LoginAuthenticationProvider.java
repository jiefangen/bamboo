package org.panda.business.official.infrastructure.security.authentication;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.official.common.constant.UserAuthConstants;
import org.panda.tech.security.authentication.AbstractAuthenticationProvider;
import org.panda.tech.security.authentication.UserSpecificDetailsAuthenticationToken;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.panda.tech.security.user.UserSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * 支持登录服务端认证提供者
 */
@Component
public class LoginAuthenticationProvider extends AbstractAuthenticationProvider<AbstractAuthenticationToken> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserSpecificDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        DefaultUserSpecificDetails userSpecificDetails = (DefaultUserSpecificDetails) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userSpecificDetails.getPassword())) {
            LogUtil.error(getClass(), UserAuthConstants.ORIGINAL_PWD_WRONG);
            throw new BadCredentialsException(UserAuthConstants.ORIGINAL_PWD_WRONG);
        }
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = webAuthenticationDetails.getRemoteAddress();
        UserSpecificDetailsAuthenticationToken authenticationToken = new UserSpecificDetailsAuthenticationToken(userSpecificDetails);
        authenticationToken.setIp(remoteAddress);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
