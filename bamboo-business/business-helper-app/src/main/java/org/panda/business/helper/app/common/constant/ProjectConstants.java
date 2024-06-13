package org.panda.business.helper.app.common.constant;

/**
 * 项目全局常量类
 *
 * @author fangen
 * @since 2020/5/16
 **/
public class ProjectConstants {
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
     * 账户已被锁定
     */
    public static final int USER_LOCKED_CODE = 5004;
    public static final String USER_LOCKED = "This account has been locked.";
    /**
     * 登出
     */
    public static final int LOGGED_OUT = 5006;
    /**
     * 登出原因
     */
    public static final String LOGGED_OUT_REASON = "Maybe the server restarted or some other reasons.";

    /**
     * 其他客户端登入
     */
    public static final Integer OTHER_CLIENTS_LOGGED_IN = 5012;

    /**
     * Token失效
     */
    public static final Integer TOKEN_EXPIRED = 4018;

    /**
     * 默认初始化密码
     */
    public static final String DEFAULT_USER_PWD = "123456";
    /**
     * 未知
     */
    public static final String DEFAULT_UNKNOWN = "unknown";

}
