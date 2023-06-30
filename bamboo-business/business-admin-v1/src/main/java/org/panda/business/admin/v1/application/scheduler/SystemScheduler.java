package org.panda.business.admin.v1.application.scheduler;

import org.panda.bamboo.common.constant.basic.Times;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.admin.v1.modules.system.service.SysActionLogService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 系统全局调度器
 *
 * @author fangen
 */
@Component
public class SystemScheduler {
    private static final Logger LOGGER = LogUtil.getLogger(SystemScheduler.class);

    @Autowired
    private SysActionLogService actionLogService;

    /**
     * 定期清除操作日志
     * 间隔一周
     */
    @Scheduled(fixedDelay= 7 * Times.MS_ONE_DAY, initialDelay=Times.MS_ONE_SECOND)
    public void cleanActionLog() {
        LOGGER.debug("CleanActionLog scheduler start...");
        int result = actionLogService.removeLogByTime(7);
        LOGGER.debug("CleanActionLog scheduler end. cleanCount: {}", result);
    }

}
