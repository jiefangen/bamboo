package org.panda.business.helper.app.common.constant;

/**
 * 项目全局常量类
 *
 * @author fangen
 * @since 2020/5/16
 **/
public class ProjectConstants {
    /**
     * 用户信息错误状态码
     */
    public static final Integer USER_INFO_ERROR = 50001;

    /**
     * 用户信息为空
     */
    public static final String USER_EMPTY = "User info is empty.";

    /**
     * 用户名不存在
     */
    public static final String USERNAME_NOT_EXIST = "Username does not exist.";

    /**
     * 密码错误
     */
    public static final String PWD_WRONG = "Wrong password.";

    /**
     * 原密码错误
     */
    public static final String ORIGINAL_PWD_WRONG = "The original password is incorrect!";

    /**
     * token验证错误Code
     */
    public static final Integer ILLEGAL_TOKEN = 50008;

    /**
     * 掉线
     */
    public static final Integer LOGGED_OUT = 50010;

    /**
     * 其他客户端登入
     */
    public static final Integer OTHER_CLIENTS_LOGGED_IN = 50012;

    /**
     * Token失效
     */
    public static final Integer TOKEN_EXPIRED = 50014;

    /**
     * token验证错误
     */
    public static final String ILLEGAL_TOKEN_FAILURE = "Token verify failure.";

    /**
     * 掉线原因
     */
    public static final String LOGGED_OUT_REASON = "Maybe the server restarted or some other reasons.";

    /**
     * 通过Token拦截器
     */
    public static final String VIA_TOKEN_INTERCEPTOR = "Via token interceptor";

    /**
     * Token认证失败
     */
    public static final String TOKEN_VERIFY_FAILURE = "Token verify failure.";

    /**
     * 传入参数不完整
     */
    public static final String PARAMETERS_INCOMPLETE = "Incoming parameters are incomplete.";

    /**
     * 没有修改密码的角色
     */
    public static final String ROLE_NOT_CHANGE_PASS = "You have no role to change someone else's password.";

    /**
     * 没有删除用户的角色
     */
    public static final String ROLE_NOT_DELETE_USER = "You have no role to delete any user.";

    /**
     * 此用户已被禁用
     */
    public static final String USER_DISABLED = "This user has been disabled.";

    /**
     * 默认初始化密码
     */
    public static final String DEFAULT_USER_PWD = "123456";

}
