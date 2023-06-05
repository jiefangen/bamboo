package org.panda.tech.security.config.exception;

import org.panda.bamboo.common.exception.business.BusinessException;
import org.springframework.security.core.AuthenticationException;

/**
 * 业务鉴权异常
 */
public class BusinessAuthenticationException extends AuthenticationException {

    private static final long serialVersionUID = -5571766088305286560L;

    public BusinessAuthenticationException(BusinessException cause) {
        super(cause.getMessage(), cause);
    }

    public BusinessAuthenticationException(String message, Object... args) {
        this(new BusinessException(message, args));
    }

}
