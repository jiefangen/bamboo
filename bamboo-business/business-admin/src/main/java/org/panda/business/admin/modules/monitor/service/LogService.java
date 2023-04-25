package org.panda.business.admin.modules.monitor.service;

import com.github.pagehelper.PageInfo;
import org.panda.business.admin.modules.monitor.model.ActionLog;
import org.panda.business.admin.modules.monitor.model.param.LogQueryParam;

public interface LogService {
    PageInfo<ActionLog> getLogPage(LogQueryParam param);

    void deleteAllLog();
}
