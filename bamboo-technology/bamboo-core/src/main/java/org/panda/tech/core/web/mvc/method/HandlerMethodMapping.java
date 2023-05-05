package org.panda.tech.core.web.mvc.method;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求处理方法映射
 */
public interface HandlerMethodMapping {

    HandlerExecutionChain getHandlerChain(HttpServletRequest request);

    default HandlerMethod getHandlerMethod(HttpServletRequest request) {
        HandlerExecutionChain chain = getHandlerChain(request);
        if (chain != null) {
            Object handler = chain.getHandler();
            if (handler instanceof HandlerMethod) {
                return (HandlerMethod) handler;
            }
        }
        return null;
    }

}
