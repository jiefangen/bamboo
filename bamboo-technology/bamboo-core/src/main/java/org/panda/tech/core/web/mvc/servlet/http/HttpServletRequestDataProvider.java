package org.panda.tech.core.web.mvc.servlet.http;

import org.panda.tech.core.web.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 基于servlet的http请求数据提供者
 */
public class HttpServletRequestDataProvider implements HttpRequestDataProvider {

    private HttpServletRequest request;

    public HttpServletRequestDataProvider(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getHeader(String name) {
        return this.request.getHeader(name);
    }

    @Override
    public Map<String, String> getHeaders() {
        return WebUtil.getHeaders(this.request);
    }

    @Override
    public String getParameter(String name) {
        return this.request.getParameter(name);
    }

    @Override
    public String[] getParameterArray(String name) {
        return this.request.getParameterValues(name);
    }

    @Override
    public Map<String, Object> getParameters() {
        return WebUtil.getRequestParameterMap(this.request);
    }

    @Override
    public String getBody() {
        return WebUtil.getRequestBodyString(this.request);
    }

}
