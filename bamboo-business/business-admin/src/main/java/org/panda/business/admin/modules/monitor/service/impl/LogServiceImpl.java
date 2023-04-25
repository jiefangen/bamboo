package org.panda.business.admin.modules.monitor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.panda.business.admin.common.constant.enums.ActionType;
import org.panda.business.admin.common.utils.DateUtil;
import org.panda.business.admin.modules.monitor.dao.ActionLogDao;
import org.panda.business.admin.modules.monitor.model.ActionLog;
import org.panda.business.admin.modules.monitor.model.param.LogQueryParam;
import org.panda.business.admin.modules.monitor.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private ActionLogDao actionLogDao;

    @Override
    public PageInfo<ActionLog> getLogPage(LogQueryParam param) {
        param.initPage();
        Page<ActionLog> actionLog = actionLogDao.findLogPage(param.getKeyword());
        actionLog.forEach(log -> {
            log.setActionDesc(ActionType.getActionDesc(log.getActionType()).get());
            log.setOperatingTimeStr(DateUtil.format(log.getOperatingTime(), DateUtil.LONG_DATE_PATTERN));
        });
        PageInfo<ActionLog> pageInfo = new PageInfo<>(actionLog);
        return pageInfo;
    }

    @Override
    public void deleteAllLog() {
        actionLogDao.truncateLog();
    }
}
