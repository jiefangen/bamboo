package org.panda.business.helper.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.business.helper.app.common.model.WebLogData;
import org.panda.business.helper.app.model.entity.AppActionLog;
import org.panda.business.helper.app.repository.AppActionLogMapper;
import org.panda.business.helper.app.service.AppActionLogService;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.restful.ResultEnum;
import org.panda.tech.core.web.util.IP2RegionUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * <p>
 * APP操作日志 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2024-06-11
 */
@Service
public class AppActionLogServiceImpl extends ServiceImpl<AppActionLogMapper, AppActionLog> implements AppActionLogService {

    @Async
    @Override
    public void intoLogDbAsync(WebLogData webLogData, Object res) {
        AppActionLog actionLog = new AppActionLog();
        String remoteAddress = webLogData.getHost();
        actionLog.setHost(remoteAddress);
        actionLog.setIpAddress(IP2RegionUtil.getIPRegion(remoteAddress));
        actionLog.setIdentity(webLogData.getIdentity());
        long startTimeMillis = webLogData.getStartTimeMillis();
        LocalDateTime startDateTime = TemporalUtil.toLocalDateTime(Instant.ofEpochMilli(startTimeMillis));
        actionLog.setOperatingTime(startDateTime);
        actionLog.setElapsedTime(webLogData.getTakeTime());
        actionLog.setActionType(webLogData.getActionType());
        actionLog.setContent(webLogData.getContent());
        actionLog.setRequestBody(webLogData.getBodyStr());
        // 返回结果解析处理
        if (res instanceof RestfulResult) {
            RestfulResult<?> result = (RestfulResult<?>) res;
            if (result.getCode() != ResultEnum.SUCCESS.getCode()) {
                actionLog.setExceptionInfo(result.getMessage());
            }
            actionLog.setStatusCode(result.getCode());
            actionLog.setResponseRes(JsonUtil.toJson(result));
        } else if (res instanceof Throwable) {
            Throwable throwable = (Throwable) res;
            actionLog.setExceptionInfo(throwable.getMessage());
            if (throwable instanceof BusinessException) {
                BusinessException businessException = (BusinessException) throwable;
                actionLog.setStatusCode(businessException.getCode());
            } else {
                actionLog.setStatusCode(ResultEnum.FAILURE.getCode());
            }
        } else {
            actionLog.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        this.save(actionLog);
    }
}
