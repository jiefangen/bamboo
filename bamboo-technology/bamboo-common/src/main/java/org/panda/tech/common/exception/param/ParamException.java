package org.panda.tech.common.exception.param;

import org.panda.tech.common.exception.AbstractRuntimeException;
import org.panda.tech.common.exception.ExceptionEnum;

/**
 * 参数异常
 */
public class ParamException extends AbstractRuntimeException {
    private static final long serialVersionUID = -6117478873288858668L;

    public ParamException(int code, String message) {
        super(code, message);
    }

    public ParamException(String message) {
        super(ExceptionEnum.PARAMETERS.getErrCode(), message);
    }

    public ParamException() {
        super(ExceptionEnum.PARAMETERS.getErrCode(), ExceptionEnum.PARAMETERS.getErrMessage());
    }
}
