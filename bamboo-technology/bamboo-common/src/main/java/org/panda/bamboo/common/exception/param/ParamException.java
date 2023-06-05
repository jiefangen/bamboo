package org.panda.bamboo.common.exception.param;

import org.panda.bamboo.common.exception.AbstractRuntimeException;
import org.panda.bamboo.common.exception.ExceptionEnum;

/**
 * 参数异常
 */
public class ParamException extends AbstractRuntimeException {

    private static final long serialVersionUID = -6117478873288858668L;

    public ParamException(int code, String message) {
        super(code, message);
    }

    public ParamException(String message) {
        super(ExceptionEnum.PARAMETERS.getCode(), message);
    }

    public ParamException() {
        super(ExceptionEnum.PARAMETERS.getCode(), ExceptionEnum.PARAMETERS.getMessage());
    }
}
