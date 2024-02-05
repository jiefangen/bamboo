package org.panda.service.auth.infrastructure.security.service;

/**
 * Auth服务器异常错误码集
 */
public class AuthServerExceptionCodes {

    private AuthServerExceptionCodes() {
    }

    /**
     * 无效的service参数
     */
    public static final String INVALID_SERVICE = "error.auth.server.invalid_service";

    /**
     * 无效的应用token
     */
    public static final String INVALID_APP_TOKEN = "error.auth.server.invalid_app_token";

    /**
     * 不支持的登录认证方式
     */
    public static final String UNSUPPORTED_AUTHENTICATE_MODE = "error.auth.server.unsupported_authenticate_mode";

}
