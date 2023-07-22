package org.panda.business.admin.common.config.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.panda.bamboo.common.exception.ExceptionEnum;
import org.panda.business.admin.common.constant.SystemConstants;
import org.panda.business.admin.modules.monitor.service.SysUserTokenService;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.jwt.InternalJwtResolver;
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
 **/
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private InternalJwtResolver jwtResolver;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return true;
        }
        String jwt = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        boolean jwtVerify;
        try {
            jwtVerify = jwtResolver.verify(jwt);
        } catch (Exception e) {
            Object obj = RestfulResult.failure(ExceptionEnum.ILLEGAL_TOKEN.getCode(), ExceptionEnum.ILLEGAL_TOKEN.getMessage());
            WebHttpUtil.buildJsonResponse(response, obj);
            return false;
        }

        boolean interceptPass = false; // 拦截器通过状态标识
        Object failureResult = RestfulResult.failure();
        if (jwtVerify) {
            LambdaQueryWrapper<SysUserToken> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUserToken::getToken, jwt);
            SysUserToken userToken = sysUserTokenService.getOne(queryWrapper, false);
            if (userToken != null && userToken.getStatus() != null) {
                Integer status = userToken.getStatus();
                if (status == 1) { // 在线有效
                    interceptPass = true;
                } else if (status == 2) { // 离线
                    userToken.setStatus(1);
                    sysUserTokenService.updateById(userToken);
                    interceptPass = true;
                } else if (status == 3) { // 失效
                    failureResult = RestfulResult.failure(ExceptionEnum.TOKEN_EXPIRED.getCode(),
                            ExceptionEnum.TOKEN_EXPIRED.getMessage());
                } else if (status == 4) { // 登出
                    failureResult = RestfulResult.failure(SystemConstants.LOGGED_OUT, SystemConstants.LOGGED_OUT_REASON);
                }
            }
        }

        if (!interceptPass) {
            WebHttpUtil.buildJsonResponse(response, failureResult);
        }
        return interceptPass;
    }
}
