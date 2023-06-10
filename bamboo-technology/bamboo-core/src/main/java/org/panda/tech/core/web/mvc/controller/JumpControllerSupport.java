package org.panda.tech.core.web.mvc.controller;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 跳转控制器支持
 */
public abstract class JumpControllerSupport {

    @RequestMapping("/http/**")
    public void http(HttpServletRequest request, HttpServletResponse response,
            @RequestBody(required = false) Map<String, Object> body) throws Exception {
        String targetUrl = getTargetUrl(NetUtil.PROTOCOL_HTTP, request);
        jump(request, response, targetUrl, body);
    }

    @RequestMapping("/https/**")
    public void https(HttpServletRequest request, HttpServletResponse response,
            @RequestBody(required = false) Map<String, Object> body) throws Exception {
        String targetUrl = getTargetUrl(NetUtil.PROTOCOL_HTTPS, request);
        jump(request, response, targetUrl, body);
    }

    @RequestMapping("/to/**")
    public void to(HttpServletRequest request, HttpServletResponse response,
            @RequestBody(required = false) Map<String, Object> body) throws Exception {
        String protocol = WebHttpUtil.getProtocol(request) + Strings.COLON + Strings.DOUBLE_SLASH;
        String targetUrl = getTargetUrl(protocol, request);
        jump(request, response, targetUrl, body);
    }

    protected String getTargetUrl(String protocol, HttpServletRequest request) {
        String url = WebHttpUtil.getRelativeRequestUrl(request);
        int index = StringUtils.ordinalIndexOf(url, Strings.SLASH, 3);
        String path = url.substring(index + 1);
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            path += Strings.QUESTION + queryString;
        }
        return protocol + path;
    }

    protected abstract void jump(HttpServletRequest request, HttpServletResponse response, String targetUrl,
            Map<String, Object> body) throws Exception;

}
