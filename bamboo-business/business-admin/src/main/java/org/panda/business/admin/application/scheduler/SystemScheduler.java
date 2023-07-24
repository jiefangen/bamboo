package org.panda.business.admin.application.scheduler;

import org.panda.bamboo.common.constant.basic.Times;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.admin.modules.monitor.service.SysActionLogService;
import org.panda.business.admin.modules.monitor.service.SysUserTokenService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    @Autowired
    private SysUserTokenService userTokenService;

    /**
     * 定期清理废弃日志
     * 间隔一周
     */
    @Scheduled(fixedDelay = 7*Times.MS_ONE_DAY, initialDelay = Times.MS_ONE_SECOND)
    public void cleanObsoleteLog() {
        LOGGER.debug("CleanActionLog scheduler start...");
        // 日志记录保存14～21天
        actionLogService.cleanObsoleteLog();
        // 记录保存时长7～14天
        userTokenService.cleanObsoleteToken();
        LOGGER.debug("CleanActionLog scheduler end.");
    }

    /**
     * 在线用户token状态刷新
     * 间隔30秒
     */
    @Async("scheduledExecutor")
    @Scheduled(fixedDelay = 30*Times.MS_ONE_SECOND, initialDelay = Times.MS_ONE_SECOND)
    public void refreshToken() {
        LOGGER.debug("RefreshToken scheduler start...");
        userTokenService.refreshTokenStatus();
        LOGGER.debug("RefreshToken scheduler end.");
    }

}
