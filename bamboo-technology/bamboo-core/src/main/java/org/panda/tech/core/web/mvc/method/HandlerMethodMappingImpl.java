package org.panda.tech.core.web.mvc.method;

import org.panda.bamboo.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求处理方法映射实现
 */
@Component
public class HandlerMethodMappingImpl implements HandlerMethodMapping {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Override
    public HandlerExecutionChain getHandlerChain(HttpServletRequest request) {
        try {
            return this.handlerMapping.getHandler(request);
        } catch (Exception e) {
            LogUtil.debug(getClass(), e); // 仅作为调试日志打印
            return null;
        }
    }

}
