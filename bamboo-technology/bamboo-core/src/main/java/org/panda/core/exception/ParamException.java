package org.panda.core.exception;

import org.panda.bamboo.common.exception.AbstractRuntimeException;

/**
 * 参数异常
 */
public class ParamException extends AbstractRuntimeException {
    private static final long serialVersionUID = 6743671808524987765L;

    public ParamException(String message) {
        super(ExceptionEnum.PARAMETERS.getErrCode(), message);
    }

    public ParamException() {
        super(ExceptionEnum.PARAMETERS.getErrCode(), ExceptionEnum.PARAMETERS.getErrMessage());
    }
}
