package org.panda.tech.core.web.support;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.model.WebLogRange;
import org.panda.tech.core.web.mvc.servlet.http.BodyRepeatableRequestWrapper;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Web日志操作记录支持
 */
public abstract class WebLogSupport {
    private static final Logger LOGGER = LogUtil.getLogger(WebLogSupport.class);

    protected static ThreadLocal<WebLogRange> threadLocal = new ThreadLocal<>();

    protected void doBefore(JoinPoint joinPoint, WebOperationLog webOperationLog) {
        WebLogRange threadInfo = new WebLogRange();
        threadInfo.setStartTimeMillis(System.currentTimeMillis());
        // 组装请求参数体中的内容
        HttpServletRequest request = SpringWebContext.getRequest();
        if (StringUtils.isEmpty(webOperationLog.content())) {
            threadInfo.setContent(WebHttpUtil.getRelativeRequestUrl(request));
        } else {
            threadInfo.setContent(webOperationLog.content());
        }
        request = new BodyRepeatableRequestWrapper(request);
        String remoteAddress = WebHttpUtil.getRemoteAddress(request);
        threadInfo.setHost(remoteAddress);
        threadInfo.setIdentity(this.getIdentity(request));
        String actionTypeValue = EnumValueHelper.getValue(webOperationLog.actionType());
        threadInfo.setActionType(actionTypeValue);
        threadInfo.setBodyStr(WebHttpUtil.getRequestBodyString(request));
        threadLocal.set(threadInfo);
        LOGGER.debug("{} starts calling: requestData={}", threadInfo.getContent(), JsonUtil.toJson(threadInfo));
    }

    protected void doAfterReturning(WebOperationLog webOperationLog, Object res) {
        WebLogRange threadInfo = threadLocal.get();
        long startTimeMillis = threadInfo.getStartTimeMillis();
        long takeTime = System.currentTimeMillis() - startTimeMillis;
        threadInfo.setTakeTime(takeTime);
        if (webOperationLog.intoStorage()) {
            this.storageLog(res);
        }
        threadLocal.remove();
        LOGGER.debug("{} end call: elapsed time={}ms,result={}", threadInfo.getContent(), takeTime, res);
    }

    protected void doAfterThrowing(WebOperationLog webOperationLog, Throwable throwable) {
        if (webOperationLog.intoStorage()) {
            this.storageLog(throwable);
        }
        threadLocal.remove();
        LOGGER.error("{} invoke exception,exception information: {}", webOperationLog.content(), throwable);
    }

    protected abstract String getIdentity(HttpServletRequest request);

    protected abstract void storageLog(Object res);
}
