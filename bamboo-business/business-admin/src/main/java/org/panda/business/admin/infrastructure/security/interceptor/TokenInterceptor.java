package org.panda.business.admin.infrastructure.security.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.panda.business.admin.common.constant.SystemConstants;
import org.panda.business.admin.common.domain.ResultVO;
import org.panda.business.admin.common.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token 拦截器
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/25
 **/
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        String token = request.getHeader(SystemConstants.AUTH_HEADER);
        if (token != null) {
            try {
                return TokenUtil.verify(token);
            } catch (Exception e) {
                if (e instanceof TokenExpiredException) {// 处理token失效异常
                    LOGGER.warn(e.getMessage());
                    ResultVO result = ResultVO.getFailure(SystemConstants.TOKEN_EXPIRED, e.getMessage());
                    response.getWriter().append(JSONObject.toJSONString(result));
                    return false;
                }
            }
        }

        ResultVO result = ResultVO.getFailure(SystemConstants.ILLEGAL_TOKEN, SystemConstants.ILLEGAL_TOKEN_FAILURE);
        try {
            LOGGER.warn(SystemConstants.TOKEN_VERIFY_FAILURE);
            response.getWriter().append(JSONObject.toJSONString(result));
        } catch (Exception e1) {
            LOGGER.error("Authentication failed: ", e1);
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return false;
    }
}
