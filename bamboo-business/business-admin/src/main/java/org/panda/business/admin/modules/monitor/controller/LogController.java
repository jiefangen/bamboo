package org.panda.business.admin.modules.monitor.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.panda.business.admin.common.constant.annotation.ControllerWebLog;
import org.panda.business.admin.common.constant.enums.ActionType;
import org.panda.business.admin.common.model.ResultVO;
import org.panda.business.admin.modules.monitor.model.ActionLog;
import org.panda.business.admin.modules.monitor.model.param.LogQueryParam;
import org.panda.business.admin.modules.monitor.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志
 *
 * @author fangen
 * @since JDK 11 2022/5/7
 */
@Api(tags = "操作日志")
@RestController
@RequestMapping("/auth/monitor/log")
public class LogController {
    @Autowired
    private LogService logService;

    @PostMapping("/page")
    @ControllerWebLog(content = "/monitor/log/page", actionType= ActionType.QUERY)
    public ResultVO page(@RequestBody LogQueryParam param){
        PageInfo<ActionLog> pageInfo = logService.getLogPage(param);
        return ResultVO.getSuccess(pageInfo);
    }

    @DeleteMapping("/empty")
    @ControllerWebLog(content = "/monitor/log/empty", actionType = ActionType.EMPTY, intoDb = true)
    public ResultVO empty(){
        logService.deleteAllLog();
        return ResultVO.getSuccess();
    }
}
