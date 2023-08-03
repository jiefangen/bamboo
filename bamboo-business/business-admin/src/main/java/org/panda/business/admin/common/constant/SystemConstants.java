package org.panda.business.admin.common.constant;

/**
 * 系统规范常量
 */
public class SystemConstants {
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
     * 没有修改密码的角色
     */
    public static final String ROLE_NOT_CHANGE_PASS = "You have no role to change someone else's password.";
    /**
     * 登出
     */
    public static final int LOGGED_OUT = 5006;
    /**
     * 登出原因
     */
    public static final String LOGGED_OUT_REASON = "Maybe the server restarted or some other reasons.";

    /**
     * 是默认值
     */
    public static final String IS_DEFAULT = "Y";
    /**
     * 不是默认值
     */
    public static final String NO_DEFAULT = "N";
}
