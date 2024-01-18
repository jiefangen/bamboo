package org.panda.tech.core.concurrent;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程池工具类
 */
public class ExecutorUtil {

    private ExecutorUtil() {
    }

    public static final String DEFAULT_EXECUTOR_BEAN_NAME = "defaultExecutor";
    public static final String SCHEDULED_EXECUTOR_BEAN_NAME = "scheduledExecutor";
    public static final int DEFAULT_CORE_POOL_SIZE = 5;

    public static ExecutorService buildDefaultExecutor() {
        return new DefaultThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE);
    }

    public static ExecutorService buildDefaultExecutor(int corePoolSize) {
        return new DefaultThreadPoolExecutor(corePoolSize);
    }

    public static ScheduledExecutorService buildScheduledExecutor(int corePoolSize) {
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern(ScheduledExecutorService.class.getSimpleName() + "-%d")
                .daemon(true).build();
        return new ScheduledThreadPoolExecutor(corePoolSize, factory);
    }

}
