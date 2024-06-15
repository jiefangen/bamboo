package org.panda.business.helper.app.application.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.panda.business.helper.app.common.constant.ProjectConstants;
import org.panda.business.helper.app.common.model.WebLogData;
import org.panda.business.helper.app.infrastructure.security.AppSecurityUtil;
import org.panda.business.helper.app.infrastructure.security.user.UserIdentityToken;
import org.panda.business.helper.app.service.AppActionLogService;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.model.WebLogRange;
import org.panda.tech.core.web.support.WebLogSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Web日志切面
 *
 * @author fangen
 **/
@Component
@Aspect
public class WebLogAspect extends WebLogSupport {

    @Autowired
    private AppActionLogService actionLogService;
    @Autowired
    private AppSecurityUtil appSecurityUtil;

    @Pointcut("execution(* org.panda.business.helper.app.controller..*.*(..))")
    public void pointcut() {}

    @Before(value = "pointcut() && @annotation(webOperationLog)")
    public void doBefore(JoinPoint joinPoint, WebOperationLog webOperationLog) {
        super.doBefore(joinPoint, webOperationLog);
    }

    @AfterReturning(value = "pointcut() && @annotation(webOperationLog)", returning = "res")
    public void doAfterReturning(WebOperationLog webOperationLog, Object res) {
        super.doAfterReturning(webOperationLog, res);
    }

    @AfterThrowing(value = "pointcut() && @annotation(webOperationLog)", throwing = "throwable")
    public void doAfterThrowing(WebOperationLog webOperationLog, Throwable throwable) {
        super.doAfterThrowing(webOperationLog, throwable);
    }

    @Override
    protected String getIdentity(HttpServletRequest request) {
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        if (StringUtils.isNotBlank(token)) { // 如果有token在拦截器中已做过验证
            UserIdentityToken userIdentityToken = appSecurityUtil.parseToken(token);
            return userIdentityToken.getIdentity();
        }
        return ProjectConstants.DEFAULT_UNKNOWN;
    }

    @Override
    protected void storageLog(Object res) {
        WebLogRange threadInfo = threadLocal.get();
        WebLogData webLogData = new WebLogData();
        webLogData.transform(threadInfo);
        // 异步持久化存储
        actionLogService.intoLogDbAsync(webLogData, res);
    }
}
