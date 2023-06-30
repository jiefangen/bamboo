package org.panda.bamboo.common.constant;

/**
 * 框架全局通用常量集
 */
public class Commons {

    private Commons() {
    }

    /** 规范返回状态码 */
    public static final int RESULT_SUCCESS_CODE = 2000;
    public static final int RESULT_FAILURE_CODE = 5000;
    /** 规范返回默认描述 */
    public static final String RESULT_SUCCESS = "success";
    public static final String RESULT_FAILURE = "failure";

    /** 未知异常错误 */
    public static final int RESULT_UNKNOWN_CODE = 9999;
    public static final String RESULT_UNKNOWN = "unknown";

    /**
     * 其它
     */
    public static final String COMMON_APPLICATION = "application";
    public static final String COMMON_CONFIG = "config";
    public static final String TABLE_PREFIX = "t_";

}
