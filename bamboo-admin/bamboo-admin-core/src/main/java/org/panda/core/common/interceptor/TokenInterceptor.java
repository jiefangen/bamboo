package org.panda.core.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.panda.common.domain.ResultVO;
import org.panda.core.common.constant.SystemConstant;
import org.panda.core.common.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        String token = request.getHeader("X-Token");
        if (token != null) {
            try {
                LOGGER.info(SystemConstant.VIA_TOKEN_INTERCEPTOR);
                return TokenUtil.verify(token);
            } catch (Exception e) {
                if (e instanceof TokenExpiredException) {// 处理token失效异常
                    ResultVO result = ResultVO.getFailure(SystemConstant.TOKEN_EXPIRED, e.getMessage());
                    response.getWriter().append(JSONObject.toJSONString(result));
                } else {// 其他异常统一处理
                    ResultVO result = ResultVO.getFailure(SystemConstant.ILLEGAL_TOKEN, SystemConstant.ILLEGAL_TOKEN_FAILE);
                    try {
                        response.getWriter().append(JSONObject.toJSONString(result));
                        LOGGER.warn(SystemConstant.TOKEN_VERIFY_FAILURE);
                    } catch (Exception e1) {
                        LOGGER.error("Authentication failed: ", e1);
                        response.sendError(500);
                    }
                }
                return false;
            }
        }
        return false;
    }
}
