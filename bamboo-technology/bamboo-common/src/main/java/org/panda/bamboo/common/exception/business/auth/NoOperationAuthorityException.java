package org.panda.bamboo.common.exception.business.auth;

import org.panda.bamboo.common.exception.ExceptionEnum;
import org.panda.bamboo.common.exception.business.BusinessException;

/**
 * 没有操作权限的异常
 */
public class NoOperationAuthorityException extends BusinessException {

    private static final long serialVersionUID = -1663613536084450295L;

    public NoOperationAuthorityException(String message) {
        super(ExceptionEnum.AUTH_NO_OPERA.getCode(), message);
    }

    public NoOperationAuthorityException() {
        super(ExceptionEnum.AUTH_NO_OPERA.getCode(), ExceptionEnum.AUTH_NO_OPERA.getMessage());
    }

}
