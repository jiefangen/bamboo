package org.panda.tech.common.exception.business;

import org.panda.tech.common.exception.AbstractBaseException;
import org.panda.tech.common.exception.ExceptionEnum;

/**
 * 业务异常基类
 **/
public class BusinessException extends AbstractBaseException {

    private static final long serialVersionUID = 7372666313625341473L;

    public BusinessException(int code, String message) {
        super(code, message);
    }

    public BusinessException(String message) {
        super(ExceptionEnum.BUSINESS.getErrCode(), message);
    }

    public BusinessException() {
        super(ExceptionEnum.BUSINESS.getErrCode(), ExceptionEnum.BUSINESS.getErrMessage());
    }
}
