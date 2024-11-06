package org.panda.business.admin.application.aspect;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.panda.business.admin.common.model.WebLogData;
import org.panda.business.admin.modules.monitor.service.SysActionLogService;
import org.panda.business.admin.modules.monitor.service.SysUserTokenService;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import org.panda.tech.core.spec.log.annotation.WebOperationLog;
import org.panda.tech.core.spec.log.support.WebLogSupport;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.model.WebLogRange;
import org.panda.tech.security.user.UserSpecificDetails;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * Web日志切面
 *
 * @author fangen
 **/
@Aspect
@Configuration
public class WebLogAspect extends WebLogSupport {

    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysActionLogService actionLogService;

    @Pointcut("execution(* org.panda.business.admin.modules..*.*(..))")
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
        UserSpecificDetails<?> userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        if (userSpecificDetails != null) {
            return userSpecificDetails.getUsername();
        }
        return null;
    }

    @Override
    protected void storageLog(Object res) {
        WebLogRange threadInfo = threadLocal.get();
        WebLogData webLogData = new WebLogData();
        webLogData.transform(threadInfo);
        // 查询交互凭证绑定操作日志
        LambdaQueryWrapper<SysUserToken> queryWrapper = Wrappers.lambdaQuery();
        HttpServletRequest request = SpringWebContext.getRequest();
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        queryWrapper.eq(SysUserToken::getToken, token);
        SysUserToken sysUserToken = sysUserTokenService.getOne(queryWrapper, false);
        if (sysUserToken != null) {
            webLogData.setSourceId(String.valueOf(sysUserToken.getId()));
        }
        // 异步写入数据库
        actionLogService.intoLogDb(webLogData, res);
    }
}
