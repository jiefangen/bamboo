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
    protected static final int ERROR_BUSINESS_CODE = 5100;
    protected static final String ERROR_BUSINESS = "Business exception!";

    /**
     * 参数异常
     */
    protected static final int ERROR_PARAMETERS_CODE = 5200;
    protected static final String ERROR_PARAMETERS = "Parameters exception!";
    protected static final int ERROR_PARAMETERS_REQUIRED_CODE = 5201;
    protected static final String ERROR_PARAMETERS_REQUIRED = "Required parameters exception!";

    /**
     * 认证鉴权异常
     */
    protected static final int ERROR_AUTH_NO_OPERA_CODE = 4003;
    protected static final String ERROR_AUTH_NO_OPERA = "No operation authority exception!";

}
