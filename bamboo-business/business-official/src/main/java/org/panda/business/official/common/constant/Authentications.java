package org.panda.business.official.common.constant;

/**
 * 认证鉴权异常规范常量
 */
public class Authentications {
    /**
     * 用户账户不存在
     */
    public static final int USER_NOT_EXIST_CODE = 5001;
    public static final String USERNAME_NOT_EXIST = "Username does not exist.";
    /**
     * 密码验证错误
     */
    public static final int PWD_WRONG_CODE = 5002;
    public static final String PWD_WRONG = "Password verification error.";
    /**
     * 账户已被禁用
     */
    public static final int USER_DISABLED_CODE = 5003;
    public static final String USER_DISABLED = "This account has been disabled.";

    /**
     * token验证失败
     */
    public static final int ILLEGAL_TOKEN_CODE = 5004;
    public static final String ILLEGAL_TOKEN = "Token verify failure.";
    /**
     * Token过期
     */
    public static final int TOKEN_EXPIRED_CODE = 5005;
    public static final String TOKEN_EXPIRED = "Token verify expired.";


    /**
     * 掉线
     */
    public static final int LOGGED_OUT = 5006;
    /**
     * 掉线原因
     */
    public static final String LOGGED_OUT_REASON = "Maybe the server restarted or some other reasons.";

}
