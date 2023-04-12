package org.panda.tech.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.tech.common.constant.Commons;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    /**
     * 参数异常，适用于各种业务异常参数
     */
	PARAMETERS(Commons.EXCEPTION_PARAMETERS, "Parameters exception!"),
    /**
     * 必传参数异常
     */
    PARAMETERS_REQUIRED(Commons.EXCEPTION_PARAMETERS_REQUIRED, "Required parameters exception!"),
    /**
     * 业务异常
     */
    BUSINESS(Commons.EXCEPTION_BUSINESS, "Business exception!"),
    ;

    private int errCode;
    private String errMessage;
    
}
