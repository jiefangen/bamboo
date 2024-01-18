package org.panda.tech.core.web.mvc.servlet.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 禁止访问过滤器
 */
public class ForbidAccessFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

}
