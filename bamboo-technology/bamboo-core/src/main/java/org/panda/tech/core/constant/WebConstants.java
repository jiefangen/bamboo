package org.panda.tech.core.constant;

/**
 * Web常量类
 */
public class WebConstants {

    private WebConstants() {
    }

    /**
     * 头信息名：AJAX请求
     */
    public static final String HEADER_AJAX_REQUEST = "X-Requested-With";

    /**
     * 带值的头信息：AJAX请求
     */
    public static final String HEADER_AJAX_REQUEST_WITH_VALUE = "X-Requested-With=XMLHttpRequest";

    /**
     * 头信息名：请求来源
     */
    public static final String HEADER_REFERER = "referer";

    /**
     * 头信息名：重定向目标地址
     */
    public static final String HEADER_REDIRECT_TO = "Redirect-To";

    /**
     * 头信息：登录地址
     */
    public static final String HEADER_LOGIN_URL = "Login-Url";
    /**
     * 头信息：原始页面
     */
    public static final String HEADER_ORIGINAL_PAGE = "Original-Page";
    /**
     * 头信息：原始请求
     */
    public static final String HEADER_ORIGINAL_REQUEST = "Original-Request";

    /**
     * 头信息名：RPC JWT
     */
    public static final String HEADER_RPC_JWT = "Rpc-Jwt";

    /**
     * 默认的跳转目标参数
     */
    public static final String DEFAULT_REDIRECT_TARGET_URL_PARAMETER = "_next";

}
