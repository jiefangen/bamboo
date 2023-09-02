package org.panda.business.admin.modules.monitor.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.modules.monitor.api.param.LogQueryParam;
import org.panda.business.admin.modules.monitor.service.SysActionLogService;
import org.panda.business.admin.modules.monitor.service.entity.SysActionLog;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志
 *
 * @author fangen
 */
@Api(tags = "系统日志管理")
@RestController
@RequestMapping("/monitor/log")
public class ActionLogController {

    @Autowired
    private SysActionLogService actionLogService;

    @PostMapping("/page")
    @ConfigPermission
    public RestfulResult page(@RequestBody LogQueryParam queryParam) {
        QueryResult<SysActionLog> actionLogPage = actionLogService.getLogByPage(queryParam);
        return RestfulResult.success(actionLogPage);
    }

    @DeleteMapping("/empty")
    @WebOperationLog(actionType = ActionType.EMPTY, intoStorage = true)
    public RestfulResult empty() {
        actionLogService.deleteAllLog();
        return RestfulResult.success();
    }
}
