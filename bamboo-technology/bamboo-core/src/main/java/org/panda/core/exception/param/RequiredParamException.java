package org.panda.core.exception.param;

import org.panda.core.exception.ExceptionEnum;

/**
 * 必传参数异常
 */
public class RequiredParamException extends ParamException {
    private static final long serialVersionUID = 7840976808524909754L;

    public RequiredParamException(String message) {
        super(ExceptionEnum.PARAMETERS_REQUIRED.getErrCode(), message);
    }

    public RequiredParamException() {
        super(ExceptionEnum.PARAMETERS_REQUIRED.getErrCode(), ExceptionEnum.PARAMETERS.getErrMessage());
    }
}