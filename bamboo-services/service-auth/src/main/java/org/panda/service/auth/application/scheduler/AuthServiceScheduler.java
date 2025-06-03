package org.panda.service.auth.application.scheduler;

import org.panda.bamboo.common.constant.Profiles;
import org.panda.bamboo.common.constant.basic.Times;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.service.auth.service.AppServiceService;
import org.panda.tech.core.concurrent.ExecutorUtil;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.data.redis.lock.RedisDistributedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 认证服务定时任务
 *
 * @author fangen
 */
@Component
public class AuthServiceScheduler {

    @Value(AppConstants.EL_SPRING_PROFILES_ACTIVE)
    private String env;

    @Autowired
    private RedisDistributedLock redisDistributedLock;
    @Autowired
    private AppServiceService appServiceService;

    /**
     * 分布式服务心跳定时器
     */
    @Async(ExecutorUtil.SCHEDULED_EXECUTOR_BEAN_NAME)
    @Scheduled(fixedDelay = 10*Times.MS_ONE_SECOND, initialDelay = Times.MS_ONE_SECOND)
    public void heartbeat() {
        if(Profiles.LOCAL.equals(env) || (Profiles.JUNIT.equals(env))) { // 开发环境关闭该任务
            return;
        }
        LogUtil.info(getClass(), "Service heartbeat scheduler start...");
        String lockKey = "heartbeat-task-lock";
        if (redisDistributedLock.tryLock(lockKey)) { // 多节点非阻塞服务健康检测
            try {
                appServiceService.checkServiceHealth();
            } finally {
                redisDistributedLock.unlock(lockKey);
            }
        } else {
            LogUtil.warn(getClass(), "The lock was not acquired, skipping this execution.");
        }
        LogUtil.info(getClass(), "Service heartbeat scheduler end.");
    }
}
