package org.panda.tech.common.exception.param;

import org.panda.tech.common.exception.ExceptionEnum;

/**
 * 必传参数异常
 */
public class RequiredParamException extends ParamException {
    private static final long serialVersionUID = -8068045982123575191L;

    public RequiredParamException(String message) {
        super(ExceptionEnum.PARAMETERS_REQUIRED.getErrCode(), message);
    }

    public RequiredParamException() {
        super(ExceptionEnum.PARAMETERS_REQUIRED.getErrCode(), ExceptionEnum.PARAMETERS.getErrMessage());
    }
}
