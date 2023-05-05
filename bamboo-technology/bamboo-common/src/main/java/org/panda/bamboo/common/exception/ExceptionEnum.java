package org.panda.bamboo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.bamboo.common.constant.Commons;

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

    /**
     * 安全认证异常
     */
    AUTH(Commons.EXCEPTION_AUTH, "Safety certificate exception!"),
    /**
     * 安全认证异常
     */
    AUTH_NO_OPERA(Commons.EXCEPTION_AUTH_NO_OPERA, "Authorization failed exception!"),
    ;

    private int errCode;
    private String errMessage;
    
}
