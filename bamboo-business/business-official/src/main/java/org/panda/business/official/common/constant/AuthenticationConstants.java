package org.panda.business.official.common.constant;

/**
 * 认证鉴权定义常量
 */
public class AuthenticationConstants {
    /**
     * 用户账户不存在（认证失败）
     */
    public static final int USER_NOT_EXIST_CODE = 4011;
    public static final String USERNAME_NOT_EXIST = "Username does not exist.";
    /**
     * 密码验证错误（认证失败）
     */
    public static final int PWD_WRONG_CODE = 4012;
    public static final String PWD_WRONG = "Password verification error.";
    /**
     * 账户已被禁用（认证失败）
     */
    public static final int USER_DISABLED_CODE = 4013;
    public static final String USER_DISABLED = "This account has been disabled.";

    /**
     * token验证失败（授权失败）
     */
    public static final int ILLEGAL_TOKEN_CODE = 4031;
    public static final String ILLEGAL_TOKEN = "Token verify failure.";
    /**
     * Token过期（授权失败）
     */
    public static final int TOKEN_EXPIRED_CODE = 4032;
    public static final String TOKEN_EXPIRED = "Token verify expired.";


    /**
     * 掉线
     */
    public static final int LOGGED_OUT = 5006;
    /**
     * 掉线原因
     */
    public static final String LOGGED_OUT_REASON = "Maybe the server restarted or some other reasons.";
    /**
     * 没有修改密码的角色
     */
    public static final String ROLE_NOT_CHANGE_PASS = "You have no role to change someone else's password.";
    /**
     * 没有删除用户的角色
     */
    public static final String ROLE_NOT_DELETE_USER = "You have no role to delete any user.";

}
