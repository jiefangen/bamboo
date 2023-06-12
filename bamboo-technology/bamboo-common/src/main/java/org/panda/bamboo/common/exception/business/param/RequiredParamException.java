package org.panda.bamboo.common.exception.business.param;

import org.panda.bamboo.common.exception.ExceptionEnum;

/**
 * 必传参数异常
 */
public class RequiredParamException extends ParamException {

    private static final long serialVersionUID = -8492545069430011056L;

    public RequiredParamException(String message) {
        super(ExceptionEnum.PARAMETERS_REQUIRED.getCode(), message);
    }

    public RequiredParamException() {
        super(ExceptionEnum.PARAMETERS_REQUIRED.getCode(), ExceptionEnum.PARAMETERS_REQUIRED.getMessage());
    }
}
