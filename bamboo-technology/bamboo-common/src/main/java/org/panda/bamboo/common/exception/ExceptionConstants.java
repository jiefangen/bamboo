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
    protected static final int EXCEPTION_UNAUTHORIZED_CODE = 4010;
    protected static final String EXCEPTION_UNAUTHORIZED = "Unauthorized exception!";
    protected static final int EXCEPTION_FORBIDDEN_CODE = 4030;
    protected static final String EXCEPTION_FORBIDDEN = "No operation authority exception!";

}
