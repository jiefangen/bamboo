package org.panda.tech.security.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.bamboo.common.constant.Commons;

@Getter
@AllArgsConstructor
public enum SecurityExceptionEnum {
    /**
     * 安全认证异常
     */
    AUTH_NO_OPERA(Commons.EXCEPTION_AUTH_NO_OPERA, "Authorization failed exception!"),
    ;

    private int code;
    private String message;
    
}
