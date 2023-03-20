package org.panda.bamboo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.bamboo.common.constant.CommonConstant;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    /**
     * 参数异常，适用于各种业务异常参数
     */
	PARAMETERS(CommonConstant.EXCEPTION_PARAMETERS, "Parameters exception!"),
    /**
     * 必传参数异常
     */
    PARAMETERS_REQUIRED(CommonConstant.EXCEPTION_PARAMETERS_REQUIRED, "Required parameters exception!"),
    ;

    private int errCode;
    private String errMessage;
    
}
