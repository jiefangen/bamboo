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
    UNAUTHORIZED(ExceptionConstants.UNAUTHORIZED_CODE, ExceptionConstants.UNAUTHORIZED),
    /**
     * Token认证校验异常
     */
    ILLEGAL_TOKEN(ExceptionConstants.ILLEGAL_TOKEN_CODE, ExceptionConstants.ILLEGAL_TOKEN),
    /**
     * Token过期失效
     */
    TOKEN_EXPIRED(ExceptionConstants.TOKEN_EXPIRED_CODE, ExceptionConstants.TOKEN_EXPIRED),
    /**
     * 没有操作权限异常
     */
    AUTH_NO_OPERA(ExceptionConstants.FORBIDDEN_CODE, ExceptionConstants.FORBIDDEN),
    ;

    private int code;
    private String message;
    
}
