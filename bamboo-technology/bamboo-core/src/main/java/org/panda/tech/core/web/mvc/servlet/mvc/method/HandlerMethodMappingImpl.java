package org.panda.tech.core.web.mvc.servlet.mvc.method;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.core.web.mvc.http.HttpAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 请求处理方法映射实现
 */
@Component
public class HandlerMethodMappingImpl implements HandlerMethodMapping {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    private Map<HttpAction, HandlerMethod> handlerMethods;

    @Override
    public HandlerExecutionChain getHandlerChain(HttpServletRequest request) {
        try {
            return this.handlerMapping.getHandler(request);
        } catch (Exception e) {
            LogUtil.debug(getClass(), e); // 仅作为调试日志打印
            return null;
        }
    }

    @Override
    public Map<HttpAction, HandlerMethod> getAllHandlerMethods() {
        ensure();
        return Collections.unmodifiableMap(this.handlerMethods);
    }

    private void ensure() {
        if (this.handlerMethods == null) {
            this.handlerMethods = new HashMap<>();
            this.handlerMapping.getHandlerMethods().forEach((request, handlerMethod) -> {
                PatternsRequestCondition condition = request.getPatternsCondition();
                if (condition != null) {
                    condition.getPatterns().forEach(pattern -> {
                        // 确保以/开头
                        if (!pattern.startsWith(Strings.SLASH)) {
                            pattern = Strings.SLASH + pattern;
                        }
                        Set<RequestMethod> requestMethods = request.getMethodsCondition().getMethods();
                        addHandlerMethod(pattern, handlerMethod, requestMethods);
                        // 如果有路径变量，则转换为通配符路径，再次缓存一次，以支持匹配通配符路径
                        String wildcardPattern = pattern
                                .replaceAll("\\{[a-zA-Z0-9_]+}", Strings.ASTERISK);
                        if (!wildcardPattern.equals(pattern)) {
                            addHandlerMethod(wildcardPattern, handlerMethod, requestMethods);
                        }
                    });
                }
            });
        }
    }

    private void addHandlerMethod(String pattern, HandlerMethod handlerMethod,
                                  Set<RequestMethod> requestMethods) {
        if (requestMethods.isEmpty()) {
            HttpAction action = new HttpAction(pattern);
            this.handlerMethods.put(action, handlerMethod);
        } else {
            requestMethods.forEach(requestMethod -> {
                HttpAction action = new HttpAction(pattern, requestMethod);
                this.handlerMethods.put(action, handlerMethod);
            });
        }
    }

    @Override
    public HandlerMethod getHandlerMethod(String uri, HttpMethod method) {
        ensure();
        HandlerMethod handlerMethod = this.handlerMethods.get(new HttpAction(uri, method));
        // 如果指定了访问方法无法取得，则尝试不指定访问方法获取，因为可能存在不限定访问方法的处理方法
        if (handlerMethod == null && method != null) {
            handlerMethod = this.handlerMethods.get(new HttpAction(uri));
        }
        return handlerMethod;
    }
}
