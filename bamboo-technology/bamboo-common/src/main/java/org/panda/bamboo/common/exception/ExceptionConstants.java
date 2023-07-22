package org.panda.bamboo.common.exception;

/**
 * 全局异常通用常量
 */
class ExceptionConstants {

    private ExceptionConstants() {
    }

    /**
     * 通用业务异常
     */
    protected static final int EXCEPTION_BUSINESS_CODE = 5100;
    protected static final String EXCEPTION_BUSINESS = "Business exception!";
    /**
     * 参数异常
     */
    protected static final int EXCEPTION_PARAMETERS_CODE = 5110;
    protected static final String EXCEPTION_PARAMETERS = "Parameters exception!";
    protected static final int EXCEPTION_PARAMETERS_REQUIRED_CODE = 5111;
    protected static final String EXCEPTION_PARAMETERS_REQUIRED = "Required parameters exception!";

    /**
     * 认证鉴权异常
     */
    protected static final int UNAUTHORIZED_CODE = 4010;
    protected static final String UNAUTHORIZED = "Unauthorized exception!";
    protected static final int FORBIDDEN_CODE = 4030;
    protected static final String FORBIDDEN = "No operation authority exception!";

    protected static final int ILLEGAL_TOKEN_CODE = 4014;
    protected static final String ILLEGAL_TOKEN = "Token verify failure!";
    protected static final int TOKEN_EXPIRED_CODE = 4018;
    protected static final String TOKEN_EXPIRED = "Token expired!";

}
