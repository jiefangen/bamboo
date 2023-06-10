package org.panda.business.official.infrastructure.security.authentication;

import org.panda.business.official.common.constant.AuthenticationConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.web.authentication.ResolvableExceptionAuthenticationFailureHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 配置异常登录认证失败的处理器
 *
 * @author fangen
 **/
@Component
public class LoginAuthenticationFailureHandler extends ResolvableExceptionAuthenticationFailureHandler {

    @Override
    protected Object getFailureResult(HttpServletRequest request, AuthenticationException exception) {
        if (exception instanceof UsernameNotFoundException) {
            return RestfulResult.failure(AuthenticationConstants.USER_NOT_EXIST_CODE, exception.getMessage());
        } else if (exception instanceof BadCredentialsException) {
            return RestfulResult.failure(AuthenticationConstants.PWD_WRONG_CODE, exception.getMessage());
        } else if (exception instanceof DisabledException) {
            return RestfulResult.failure(AuthenticationConstants.USER_DISABLED_CODE, exception.getMessage());
        }
        return null;
    }

}
