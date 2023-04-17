package org.panda.business.admin.core.security.cors;

import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Springboot shiro跨域处理
 *
 * @author fangen
 * @since JDK 11 2022/4/14
 */
@Component
public class CorsFilter extends OncePerRequestFilter {
    public void doFilterInternal(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        // 不可以放在后面
        response.setHeader("XDomainRequestAllowed","1");
        // 放行所有,类似*,这里的*完全无效
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 允许请求方式
        response.setHeader("Access-Control-Allow-Methods", "POST,PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        // 需要放行header头部字段 如需鉴权字段，自行添加，如Authorization等
        response.setHeader("Access-Control-Allow-Headers",
                "content-type,x-requested-with,Authorization," +
                        "authorization,Origin,No-Cache,X-Requested-With,If-Modified-Since," +
                        " Pragma, Last-Modified, Cache-Control,Expires, Content-Type, X-E4M-With,token");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        // 请求预检放行
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return;
        } else {
            chain.doFilter(request, response);
        }
    }

}
