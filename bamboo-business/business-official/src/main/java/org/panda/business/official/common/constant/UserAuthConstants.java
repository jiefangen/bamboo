package org.panda.business.official.common.constant;

/**
 * 用户认证权限常量集
 */
public class UserAuthConstants {
    /**
     * 认证授权header
     */
    public static final String AUTH_HEADER = "Authorization";
    /**
     * 用户认证角色前缀
     */
    public static final String ROLE_PREFIX = "ROLE_";

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
     * 用户信息错误状态码
     */
    public static final int USER_INFO_ERROR = 5001;
    /**
     * token验证错误Code
     */
    public static final int ILLEGAL_TOKEN = 5002;
    /**
     * 掉线
     */
    public static final int LOGGED_OUT = 5003;
    /**
     * Token过期
     */
    public static final int TOKEN_EXPIRED = 5004;
    /**
     * 其他客户端登入
     */
    public static final int OTHER_CLIENTS_LOGGED_IN = 5010;


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
    public static final String VIA_TOKEN_INTERCEPTOR = "Via token interceptor.";
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
}
