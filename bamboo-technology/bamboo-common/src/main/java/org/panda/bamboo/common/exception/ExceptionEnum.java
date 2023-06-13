package org.panda.bamboo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    /**
     * 业务异常
     */
    BUSINESS(ExceptionConstants.EXCEPTION_BUSINESS_CODE, ExceptionConstants.EXCEPTION_BUSINESS),

    /**
     * 参数异常，适用于各种业务异常参数
     */
	PARAMETERS(ExceptionConstants.EXCEPTION_PARAMETERS_CODE, ExceptionConstants.EXCEPTION_PARAMETERS),
    /**
     * 必传参数异常
     */
    PARAMETERS_REQUIRED(ExceptionConstants.EXCEPTION_PARAMETERS_REQUIRED_CODE, ExceptionConstants.EXCEPTION_PARAMETERS_REQUIRED),

    /**
     * 未认证异常
     */
    UNAUTHORIZED(ExceptionConstants.EXCEPTION_UNAUTHORIZED_CODE, ExceptionConstants.EXCEPTION_UNAUTHORIZED),
    /**
     * 没有操作权限异常
     */
    AUTH_NO_OPERA(ExceptionConstants.EXCEPTION_FORBIDDEN_CODE, ExceptionConstants.EXCEPTION_FORBIDDEN),
    ;

    private int code;
    private String message;
    
}
