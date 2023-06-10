package org.panda.bamboo.common.exception.business;

import org.panda.bamboo.common.exception.AbstractRuntimeException;
import org.panda.bamboo.common.exception.ExceptionEnum;

/**
 * 业务异常基类
 **/
public class BusinessException extends AbstractRuntimeException {

    private static final long serialVersionUID = 4785249953461996288L;

    private Object[] args;

    public BusinessException(String message, Object... args) {
        super(message);
        this.message = message;
        this.args = args;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public BusinessException(int code, String message) {
        super(code, message);
    }

    public BusinessException(String message) {
        super(ExceptionEnum.BUSINESS.getCode(), message);
    }

    public BusinessException() {
        super(ExceptionEnum.BUSINESS.getCode(), ExceptionEnum.BUSINESS.getMessage());
    }
}
