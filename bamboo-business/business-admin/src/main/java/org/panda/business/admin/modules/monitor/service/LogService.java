package org.panda.business.admin.modules.monitor.service;

import com.github.pagehelper.PageInfo;
import org.panda.business.admin.modules.monitor.domain.ActionLog;
import org.panda.business.admin.modules.monitor.domain.param.LogQueryParam;

public interface LogService {
    PageInfo<ActionLog> getLogPage(LogQueryParam param);

    void deleteAllLog();
}
