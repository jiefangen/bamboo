package org.panda.tech.security.web;

import javax.servlet.http.HttpServletRequest;

/**
 * 安全相关地址提供者
 */
public interface SecurityUrlProvider {

    /**
     * 获取默认的登录地址
     *
     * @return 默认的登录地址
     */
    default String getDefaultLoginUrl() {
        return "/login";
    }

    /**
     * 根据请求获取登录地址
     *
     * @param request 请求
     * @return 登录地址
     */
    default String getLoginUrl(HttpServletRequest request) {
        return getDefaultLoginUrl();
    }

    /**
     * 获取登出处理地址
     *
     * @return 登出处理地址
     */
    default String getLogoutProcessUrl() {
        return "/logout";
    }

    /**
     * 获取登出成功后的跳转地址
     *
     * @return 登出成功后的跳转地址
     */
    default String getLogoutSuccessUrl() {
        return getDefaultLoginUrl();
    }

}
