package org.panda.bamboo.common.exception.business.auth;

import org.panda.bamboo.common.exception.ExceptionEnum;
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
        super(ExceptionEnum.AUTH_NO_OPERA.getCode(), message);
    }

    public NoOperationAuthorityException() {
        super(ExceptionEnum.AUTH_NO_OPERA.getCode(), ExceptionEnum.AUTH_NO_OPERA.getMessage());
    }

}
