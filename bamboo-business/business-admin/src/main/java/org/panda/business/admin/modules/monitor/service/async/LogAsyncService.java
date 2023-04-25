package org.panda.business.admin.modules.monitor.service.async;

import org.panda.business.admin.modules.monitor.dao.ActionLogDao;
import org.panda.business.admin.modules.monitor.model.ActionLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 日志异步服务
 *
 * @author fangen
 * @since JDK 11 2022/5/15
 */
@Service
public class LogAsyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAsyncService.class);

    @Autowired
    private ActionLogDao actionLogDao;

    @Async("taskExecutor")
    public void intoLogDb(ActionLog actionLog) {
        LOGGER.debug("IntoLogDb asyncTask start...");
        actionLogDao.insertLog(actionLog);
        LOGGER.debug("IntoLogDb asyncTask end");
    }
}
