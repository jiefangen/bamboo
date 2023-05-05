package org.panda.bamboo.common.constant;

/**
 * 通用常量集
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

    /** 规范异常定义状态码 */
    public static final int EXCEPTION_PARAMETERS = 5100;
    public static final int EXCEPTION_PARAMETERS_REQUIRED = 5101;

    public static final int EXCEPTION_BUSINESS = 5200;

    public static final int EXCEPTION_AUTH = 4000;
    public static final int EXCEPTION_AUTH_NO_OPERA = 4003;


    /**
     * 通用技术专业英文
     */
    public static final String COMMON_YES = "yes";
    public static final String COMMON_NO = "no";
    public static final String COMMON_CONFIG = "config";
    public static final String COMMON_APPLICATION = "application";
    public static final String COMMON_MASTER = "master";
    public static final String COMMON_SLAVE = "slave";
    public static final String COMMON_PRIMARY = "primary";
    public static final String COMMON_SECONDARY = "secondary";

    /**
     * 其他
     */
    public static final String TABLE_PREFIX = "t_";

}
