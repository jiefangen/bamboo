package org.panda.bamboo.common.exception.business.security;

import org.panda.bamboo.common.exception.ExceptionEnum;
import org.panda.bamboo.common.exception.business.BusinessException;

/**
 * 没有操作权限的异常
 */
public class NoOperationAuthorityException extends BusinessException {

    private static final long serialVersionUID = -1443043734306415676L;

    public NoOperationAuthorityException(int code, String message) {
        super(code, message);
    }

    public NoOperationAuthorityException(String message) {
        super(ExceptionEnum.AUTH_NO_OPERA.getErrCode(), message);
    }

    public NoOperationAuthorityException() {
        super(ExceptionEnum.AUTH_NO_OPERA.getErrCode(), ExceptionEnum.AUTH_NO_OPERA.getErrMessage());
    }

}
