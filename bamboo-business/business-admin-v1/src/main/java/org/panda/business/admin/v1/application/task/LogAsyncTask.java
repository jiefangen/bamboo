package org.panda.business.admin.v1.application.task;

import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.business.admin.v1.common.model.WebLogData;
import org.panda.business.admin.v1.modules.system.service.SysActionLogService;
import org.panda.business.admin.v1.modules.system.service.entity.SysActionLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.restful.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 日志异步任务
 *
 * @author fangen
 */
@Service
public class LogAsyncTask {

    @Autowired
    private SysActionLogService actionLogService;

    @Async("taskExecutor")
    public void intoLogDb(WebLogData webLogData, Object res) {
        SysActionLog actionLog = new SysActionLog();
        actionLog.setRemoteAddress(webLogData.getRemoteAddress());
        actionLog.setIdentity(webLogData.getIdentity());
        long startTimeMillis = webLogData.getStartTimeMillis();
        LocalDateTime startDateTime = TemporalUtil.toLocalDateTime(Instant.ofEpochMilli(startTimeMillis));
        actionLog.setOperatingTime(startDateTime);
        actionLog.setElapsedTime(webLogData.getTakeTime());

        actionLog.setActionType(webLogData.getActionType());
        actionLog.setContent(webLogData.getContent());
        if (res instanceof RestfulResult) {
            RestfulResult result = (RestfulResult) res;
            if (result.getCode() != ResultEnum.SUCCESS.getCode()) {
                actionLog.setExceptionInfo(result.getMessage());
            }
            actionLog.setStatusCode(result.getCode());
        } else if (res instanceof Throwable) {
            Throwable throwable = (Throwable) res;
            actionLog.setExceptionInfo(throwable.getMessage());
            actionLog.setStatusCode(ResultEnum.FAILURE.getCode());
        } else {
            actionLog.setStatusCode(ResultEnum.SUCCESS.getCode());
        }
        actionLogService.save(actionLog);
    }
}
