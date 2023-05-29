package org.panda.bamboo.common.exception.business;

import org.panda.bamboo.common.exception.AbstractBaseException;
import org.panda.bamboo.common.exception.ExceptionEnum;

/**
 * 业务异常基类
 **/
public class BusinessException extends AbstractBaseException {

    private static final long serialVersionUID = 4785249953461996288L;

    private Object[] args;

    public BusinessException(String codeStr, Object... args) {
        super(codeStr);
        this.codeStr = codeStr;
        this.args = args;
    }

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
