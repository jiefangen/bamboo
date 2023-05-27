package org.panda.tech.core.web.mvc.cors;

import org.panda.tech.core.web.mvc.servlet.mvc.method.HandlerMethodMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 忽略空配置的Cors处理器
 */
@Component
public class IgnoreNullConfigCorsProcessor extends DefaultCorsProcessor {

    private HandlerMethodMapping handlerMethodMapping;

    @Autowired
    @Lazy // 避免循环依赖
    public void setHandlerMethodMapping(HandlerMethodMapping handlerMethodMapping) {
        this.handlerMethodMapping = handlerMethodMapping;
    }

    @Override
    public boolean processRequest(CorsConfiguration config, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (config == null) {
            HandlerMethod handlerMethod = this.handlerMethodMapping.getHandlerMethod(request);
            if (handlerMethod == null) {
                return true;
            }
        }
        return super.processRequest(config, request, response);
    }

}
