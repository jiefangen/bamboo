package org.panda.tech.security.config.exception;

import org.panda.bamboo.common.exception.business.BusinessException;
import org.springframework.security.core.AuthenticationException;

/**
 * 业务鉴权异常
 */
public class BusinessAuthenticationException extends AuthenticationException {

    private static final long serialVersionUID = 623069682664666369L;

    public BusinessAuthenticationException(BusinessException cause) {
        super(cause.getCodeStr(), cause);
    }

    public BusinessAuthenticationException(String code, Object... args) {
        this(new BusinessException(code, args));
    }

}
