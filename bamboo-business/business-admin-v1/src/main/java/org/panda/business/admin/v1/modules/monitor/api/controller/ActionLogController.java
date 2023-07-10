package org.panda.business.admin.v1.modules.monitor.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.v1.common.constant.Authority;
import org.panda.business.admin.v1.modules.monitor.api.param.LogQueryParam;
import org.panda.business.admin.v1.modules.monitor.service.SysActionLogService;
import org.panda.business.admin.v1.modules.monitor.service.entity.SysActionLog;
import org.panda.tech.core.spec.enums.ActionType;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigAuthorities;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志
 *
 * @author fangen
 */
@Api(tags = "监控操作日志")
@RestController
@RequestMapping("/monitor/log")
public class ActionLogController {

    @Autowired
    private SysActionLogService actionLogService;

    @PostMapping("/page")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_ACTUATOR),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "monitor_log_page")
    })
    @WebOperationLog(actionType= ActionType.QUERY)
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