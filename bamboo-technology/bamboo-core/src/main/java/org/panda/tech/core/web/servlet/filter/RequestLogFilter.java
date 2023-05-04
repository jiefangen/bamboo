package org.panda.tech.core.web.servlet.filter;

import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.web.servlet.http.BodyRepeatableRequestWrapper;
import org.panda.tech.core.web.util.WebUtil;
import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 请求日志打印过滤器
 */
public class RequestLogFilter implements Filter {

    private String[] urlPatterns;

    private Logger logger = LogUtil.getLogger(getClass());

    public RequestLogFilter(String... urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        if (ArrayUtils.isNotEmpty(this.urlPatterns)) {
            HttpServletRequest request = (HttpServletRequest) req;
            String url = WebUtil.getRelativeRequestUrl(request);
            if (StringUtil.antPathMatchOneOf(url, this.urlPatterns)) {
                request = new BodyRepeatableRequestWrapper(request);
                this.logger.info("====== request from {} ======", WebUtil.getRemoteAddress(request));
                this.logger.info("{} {}", request.getMethod(), url);
                this.logger.info("headers: {}", JsonUtil.toJson(WebUtil.getHeaders(request)));
                this.logger.info("parameters: {}", JsonUtil.toJson(WebUtil.getRequestParameterMap(request)));
                this.logger.info("body: {}", WebUtil.getRequestBodyString(request));
                req = request;
            }
        }
        chain.doFilter(req, resp);
    }

}
