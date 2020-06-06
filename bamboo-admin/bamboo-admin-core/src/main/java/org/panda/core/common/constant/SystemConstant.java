package org.panda.core.common.constant;

/**
 * 系统模块常量类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/16
 **/
public class SystemConstant {

    /**
     * 用户信息错误状态码
     */
    public static final Integer USER_INFO_ERROR = 50001;

    /**
     * 用户信息为空
     */
    public static  final String USER_EMPTY = "User info is empty.";

    /**
     * 用户名不存在
     */
    public static  final String USERNAME_NOT_EXIST = "Username does not exist.";

    /**
     * 密码错误
     */
    public static  final String PWD_WRONG = "Wrong password.";

    /**
     * 原密码错误
     */
    public static  final String ORIGINAL_PWD_WRONG = "The original password is incorrect!";

    /**
     * token验证错误Code
     */
    public static final Integer ILLEGAL_TOKEN = 50008;

    /**
     * Token expired
     */
    public static final Integer TOKEN_EXPIRED = 50014;

    /**
     * token验证错误
     */
    public static  final String ILLEGAL_TOKEN_FAILE = "Token verify failure.";

    /**
     * 通过Token拦截器
     */
    public static  final String VIA_TOKEN_INTERCEPTOR = "Via token interceptor";

    /**
     * Token认证失败
     */
    public static  final String TOKEN_VERIFY_FAILURE = "Token verify failure.";


}
