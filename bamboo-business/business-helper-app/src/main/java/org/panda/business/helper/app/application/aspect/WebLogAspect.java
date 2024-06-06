package org.panda.business.helper.app.application.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.model.WebLogRange;
import org.panda.tech.core.web.support.WebLogSupport;
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
        return null;
    }

    @Override
    protected void storageLog(Object res) {
        WebLogRange threadInfo = threadLocal.get();
        LogUtil.info(getClass(), JsonUtil.toJson(threadInfo));
    }
}
