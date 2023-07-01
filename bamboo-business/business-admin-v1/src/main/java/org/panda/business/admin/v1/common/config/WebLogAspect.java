package org.panda.business.admin.v1.common.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.panda.business.admin.v1.application.task.LogAsyncTask;
import org.panda.business.admin.v1.common.model.WebLogData;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.model.WebLogRange;
import org.panda.tech.core.web.support.WebLogSupport;
import org.panda.tech.security.user.UserSpecificDetails;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Web日志切面
 *
 * @author fangen
 **/
@Aspect
@Component
public class WebLogAspect extends WebLogSupport {

    @Autowired
    private LogAsyncTask logAsyncTask;

    @Pointcut("execution(* org.panda.business.admin.v1.modules..*.*(..))")
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
        UserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        if (userSpecificDetails != null) {
            return userSpecificDetails.getUsername();
        }
        return null;
    }

    @Override
    protected void storageLog(Object res) {
        WebLogRange threadInfo = super.threadLocal.get();
        WebLogData webLogData = new WebLogData();
        webLogData.transform(threadInfo);
        // 异步写入数据库
        logAsyncTask.intoLogDb(webLogData, res);
    }
}