package org.panda.business.admin.modules.common.config;

/**
 * 系统参数key
 *
 * @author fangen
 **/
public class SettingsKeys {
    /**
     * 应用范围-admin
     */
    public static final String APP_RANGE_ADMIN = "admin";

    /**
     * admin系统-生成token签名密钥key
     */
    public static final String TOKEN_KEY = "admin:app:tokenKey";
    /**
     * admin系统-token失效时间间隔
     */
    public static final String TOKEN_INTERVAL = "admin:app:tokenInterval";
    /**
     * admin系统-权限自动加载url通配符
     */
    public static final String AUTH_URL_PATTERNS = "admin:app:authUrlPatterns";
    /**
     * admin系统-同时在线用户限制
     */
    public static final String ONLINE_LIMIT = "admin:user:onlineLimit";
    /**
     * 用户管理-账号初始化密码
     */
    public static final String INIT_PWD = "admin:user:initPassword";

    /**
     * 通用国际化字典key前缀
     */
    public static final String I18N_KEY_PREFIX = "common_language_";
}
