package org.panda.tech.core.concurrent;

import org.panda.bamboo.common.util.LogUtil;
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 默认线程池执行器
 */
public class DefaultThreadPoolExecutor extends ThreadPoolExecutor implements DisposableBean {

    private int logPerTaskCount = 10;

    public DefaultThreadPoolExecutor(int corePoolSize, int maxPoolSize) {
        super(corePoolSize, maxPoolSize, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        setRejectedExecutionHandler(new CallerRunsPolicy());
        // 允许核心线程空闲超时退出，超时时间默认为10秒
        allowCoreThreadTimeOut(true);
        setKeepAliveTime(10, TimeUnit.SECONDS);
    }

    public DefaultThreadPoolExecutor(int corePoolSize) {
        this(corePoolSize, corePoolSize * 10);
    }

    public void setLogPerTaskCount(int logPerTaskCount) {
        if (logPerTaskCount > 0) {
            this.logPerTaskCount = logPerTaskCount;
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        long completedTaskCount = getCompletedTaskCount();
        if (completedTaskCount % this.logPerTaskCount == 0) {
            LogUtil.info(getClass(), "Thread pool:size={}, largest={}, active={}, completed={}",
                    getPoolSize(), getLargestPoolSize(), getActiveCount(), completedTaskCount);
        }
    }

    @Override
    public void destroy() throws Exception {
        shutdown();
    }

}
