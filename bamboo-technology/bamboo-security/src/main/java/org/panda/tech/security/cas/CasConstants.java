package org.panda.tech.security.cas;

/**
 * CAS常量集
 */
public class CasConstants {

    private CasConstants() {
    }

    public static final String URL_LOGIN = "/login";

    public static final String COOKIE_TGT = "CAS-TGC";

    /**
     * 参数：服务
     */
    public static final String PARAMETER_SERVICE = "service";

    /**
     * 参数：范围
     */
    public static final String PARAMETER_SCOPE = "scope";

    /**
     * 参数：票据
     */
    public static final String PARAMETER_ARTIFACT = "ticket";

}
