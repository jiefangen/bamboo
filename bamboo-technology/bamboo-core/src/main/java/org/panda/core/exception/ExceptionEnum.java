package org.panda.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    /**
     * 参数异常，适用于各种业务异常参数
     */
	PARAMETERS(5100, "Parameters exception!"),
    /**
     * 必传参数异常
     */
    PARAMETERS_REQUIRED(5101, "Required parameters exception!"),
    ;

    private Integer errCode;
    private String errMessage;
    
}
