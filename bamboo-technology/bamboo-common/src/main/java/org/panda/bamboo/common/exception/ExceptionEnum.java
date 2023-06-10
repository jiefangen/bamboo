package org.panda.bamboo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    /**
     * 业务异常
     */
    BUSINESS(ExceptionConstants.ERROR_BUSINESS_CODE, ExceptionConstants.ERROR_BUSINESS),

    /**
     * 参数异常，适用于各种业务异常参数
     */
	PARAMETERS(ExceptionConstants.ERROR_PARAMETERS_CODE, ExceptionConstants.ERROR_PARAMETERS),
    /**
     * 必传参数异常
     */
    PARAMETERS_REQUIRED(ExceptionConstants.ERROR_PARAMETERS_REQUIRED_CODE, ExceptionConstants.ERROR_PARAMETERS_REQUIRED),

    /**
     * 没有操作权限异常
     */
    AUTH_NO_OPERA(ExceptionConstants.ERROR_AUTH_NO_OPERA_CODE, ExceptionConstants.ERROR_AUTH_NO_OPERA),
    ;

    private int code;
    private String message;
    
}
