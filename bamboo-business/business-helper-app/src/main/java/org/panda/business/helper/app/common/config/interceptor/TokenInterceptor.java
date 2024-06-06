package org.panda.business.helper.app.common.config.interceptor;

import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.jwt.internal.resolver.InternalJwtResolver;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token拦截器
 *
 * @author fangen
 * @since 2020/5/25
 **/
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private InternalJwtResolver jwtResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return true;
        }

        // token结构规则校验
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        boolean jwtVerify;
        Object failureResult = RestfulResult.getFailure(ExceptionEnum.ILLEGAL_TOKEN);
        try {
            jwtVerify = jwtResolver.verify(token);
        } catch (Exception e) {
            WebHttpUtil.buildJsonResponse(response, failureResult);
            return false;
        }
        // token状态信息校验
        boolean interceptPass = false; // 拦截器通过状态标识
        if (jwtVerify) {
            // TODO 处理用户token状态逻辑
            interceptPass = true;
        }

        // token拦截器校验结果组装返回
        if (!interceptPass) {
            WebHttpUtil.buildJsonResponse(response, failureResult);
        }
        return interceptPass;
    }
}
