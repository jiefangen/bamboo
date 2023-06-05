package org.panda.tech.security.config.exception;

import org.panda.bamboo.common.exception.business.BusinessException;

/**
 * 没有操作权限的异常
 */
public class NoOperationAuthorityException extends BusinessException {

    private static final long serialVersionUID = -2590909164816415039L;

    public NoOperationAuthorityException(int code, String message) {
        super(code, message);
    }

    public NoOperationAuthorityException(String message) {
        super(SecurityExceptionEnum.AUTH_NO_OPERA.getCode(), message);
    }

    public NoOperationAuthorityException() {
        super(SecurityExceptionEnum.AUTH_NO_OPERA.getCode(), SecurityExceptionEnum.AUTH_NO_OPERA.getMessage());
    }

}
