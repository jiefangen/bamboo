package org.panda.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    /**
     * 参数异常
     */
	PARAMETERS(5101, "Parameters exception!"),
    ;

    private Integer errCode;
    private String errMessage;
    
}
