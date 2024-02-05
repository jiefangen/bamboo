package org.panda.service.auth.common.constant;

/**
 * 账户认证规范常量
 */
public class AuthConstants {
    /**
     * 用户账户不存在
     */
    public static final int ACCOUNT_NOT_EXIST_CODE = 5001;
    public static final String ACCOUNT_NOT_EXIST = "Account does not exist.";
    /**
     * 凭证验证失败
     */
    public static final int BAD_CREDENTIALS_CODE = 5002;
    public static final String PWD_WRONG = "Password verification error.";
    /**
     * 账户已被禁用
     */
    public static final int ACCOUNT_DISABLED_CODE = 5003;
    public static final String ACCOUNT_DISABLED = "This account has been disabled.";
    /**
     * 账户已被锁定
     */
    public static final int ACCOUNT_LOCKED_CODE = 5004;
    public static final String ACCOUNT_LOCKED = "This account has been locked.";
}
