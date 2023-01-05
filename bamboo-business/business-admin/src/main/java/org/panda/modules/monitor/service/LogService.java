package org.panda.modules.monitor.service;

import com.github.pagehelper.PageInfo;
import org.panda.modules.monitor.domain.ActionLog;
import org.panda.modules.monitor.domain.param.LogQueryParam;

public interface LogService {
    PageInfo<ActionLog> getLogPage(LogQueryParam param);

    void deleteAllLog();
}
