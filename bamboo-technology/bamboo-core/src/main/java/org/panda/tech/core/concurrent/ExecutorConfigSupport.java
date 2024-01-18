package org.panda.tech.core.concurrent;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public abstract class ExecutorConfigSupport {

    @Primary
    @Bean(name = ExecutorUtil.DEFAULT_EXECUTOR_BEAN_NAME, destroyMethod = "shutdown")
    @ConditionalOnMissingBean(name = ExecutorUtil.DEFAULT_EXECUTOR_BEAN_NAME)
    public ExecutorService defaultExecutor() {
        return ExecutorUtil.buildDefaultExecutor(getCorePoolSize());
    }

    protected int getCorePoolSize() {
        return ExecutorUtil.DEFAULT_CORE_POOL_SIZE;
    }

    @Bean(name = ExecutorUtil.SCHEDULED_EXECUTOR_BEAN_NAME, destroyMethod = "shutdown")
    @ConditionalOnMissingBean(name = ExecutorUtil.SCHEDULED_EXECUTOR_BEAN_NAME)
    public ScheduledExecutorService scheduledExecutor() {
        return ExecutorUtil.buildScheduledExecutor(getCorePoolSize());
    }

}
