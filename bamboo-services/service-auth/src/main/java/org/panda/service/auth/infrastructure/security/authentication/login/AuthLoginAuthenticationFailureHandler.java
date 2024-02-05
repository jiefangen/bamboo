package org.panda.service.auth.infrastructure.security.authentication.login;

import org.panda.service.auth.common.constant.AuthConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.exception.BusinessAuthenticationException;
import org.panda.tech.security.web.authentication.ResolvableExceptionAuthenticationFailureHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
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
public class AuthLoginAuthenticationFailureHandler extends ResolvableExceptionAuthenticationFailureHandler {

    @Override
    protected Object getFailureResult(HttpServletRequest request, AuthenticationException exception) {
        if (exception instanceof UsernameNotFoundException) {
            return RestfulResult.failure(AuthConstants.ACCOUNT_NOT_EXIST_CODE, exception.getMessage());
        } else if (exception instanceof BadCredentialsException) {
            return RestfulResult.failure(AuthConstants.BAD_CREDENTIALS_CODE, exception.getMessage());
        } else if (exception instanceof DisabledException) {
            return RestfulResult.failure(AuthConstants.ACCOUNT_DISABLED_CODE, exception.getMessage());
        } else if (exception instanceof LockedException) {
            return RestfulResult.failure(AuthConstants.ACCOUNT_LOCKED_CODE, exception.getMessage());
        } else if (exception instanceof BusinessAuthenticationException) {
            return RestfulResult.failure(AuthConstants.BAD_CREDENTIALS_CODE, exception.getMessage());
        }
        return null;
    }

}
