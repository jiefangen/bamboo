package org.panda.business.admin.application.scheduler;

import org.panda.business.admin.common.constant.TimeConstants;
import org.panda.business.admin.modules.facade.dao.TodoDao;
import org.panda.business.admin.modules.monitor.dao.ActionLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 系统全局调度器
 *
 * @author fangen
 * @since JDK 11 2022/5/15
 */
@Component
public class SystemScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemScheduler.class);

    @Autowired
    private ActionLogDao actionLogDao;

    @Autowired
    private TodoDao todoDao;

    /**
     * 定期清除操作日志
     * 间隔一周
     */
    @Scheduled(fixedDelay= 7 * TimeConstants.ONE_DAY, initialDelay=TimeConstants.ONE_SECOND)
    public void cleanActionLog() {
        LOGGER.debug("CleanActionLog scheduler start...");
        int result = actionLogDao.deleteLogByTime(7);
        LOGGER.debug("CleanActionLog scheduler end. result:{}", result);
    }

    /**
     * 定期清除已废弃的代办事项
     * 间隔3天
     */
    @Scheduled(fixedDelay= 3 * TimeConstants.ONE_DAY, initialDelay=TimeConstants.ONE_MINUTE)
    public void cleanDiscardTodo() {
        LOGGER.debug("CleanDiscardTodo scheduler start...");
        int result = todoDao.deleteTodoByTime(3);
        LOGGER.debug("CleanDiscardTodo scheduler end. result:{}", result);
    }
}
