package org.panda.business.helper.app.common.config.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.helper.app.model.entity.AppUserToken;
import org.panda.business.helper.app.service.AppUserTokenService;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.exception.business.auth.AuthConstants;
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
import java.time.LocalDateTime;

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
    @Autowired
    private AppUserTokenService userTokenService;

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
            LogUtil.error(getClass(), e);
            WebHttpUtil.buildJsonResponse(response, failureResult);
            return false;
        }
        // token状态信息校验
        boolean interceptPass = false; // 拦截器通过状态标识
        if (jwtVerify) {
            LambdaQueryWrapper<AppUserToken> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(AppUserToken::getToken, token);
            AppUserToken userToken = userTokenService.getOne(queryWrapper, false);
            if (userToken != null && userToken.getStatus() != null) { // 失效
                Integer status = userToken.getStatus();
                if (status == 3 || LocalDateTime.now().isAfter(userToken.getExpirationTime())) {
                    failureResult = RestfulResult.getFailure(ExceptionEnum.TOKEN_EXPIRED);
                } else {
                    if (status == 1) { // 在线有效
                        interceptPass = true;
                    } else if (status == 2) { // 离线
                        userToken.setStatus(1);
                        interceptPass = true;
                    } else if (status == 4) { // 登出
                        failureResult = RestfulResult.failure(AuthConstants.LOGGED_OUT, AuthConstants.LOGGED_OUT_REASON);
                    }
                }
                if (interceptPass) { // 实时刷新用户token在线时间
                    userToken.setUpdateTime(LocalDateTime.now());
                    userTokenService.updateById(userToken);
                }
            }
        }

        // token拦截器校验结果组装返回
        if (!interceptPass) {
            WebHttpUtil.buildJsonResponse(response, failureResult);
        }
        return interceptPass;
    }
}
