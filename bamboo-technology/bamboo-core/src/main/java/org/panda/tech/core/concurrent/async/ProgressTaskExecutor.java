package org.panda.tech.core.concurrent.async;

import org.panda.bamboo.common.util.LogUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 进度任务执行器，为保证执行定时清理，必须创建子类并实例化Bean
 *
 * @param <P> 进度类型
 */
@EnableScheduling
public abstract class ProgressTaskExecutor<P extends TaskProgress<K>, K extends Serializable> {

    private ExecutorService executor;
    private Map<K, P> progresses = new Hashtable<>();

    public ProgressTaskExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public K submit(ProgressTask<P> task) {
        P progress = task.getProgress();
        K progressId = progress.getId();
        this.progresses.put(progressId, progress);
        this.executor.submit(task);
        return progressId;
    }

    public boolean isProgressing(K progressId) {
        P progress = getProgress(progressId);
        return progress != null && progress.isRunning();
    }

    public P getProgress(K progressId) {
        P progress = this.progresses.get(progressId);
        if (progress != null && progress.isCleanable()) {
            progress = null;
            remove(progressId);
        }
        return progress;
    }

    public void remove(K progressId) {
        if (this.progresses.remove(progressId) != null) {
            LogUtil.info(getClass(), "====== The completed task progress(id={}) has been removed.", progressId);
        }
    }

    /**
     * 清理掉所有已完成的任务进度
     */
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分钟执行一次
    public void clean() {
        Collection<P> progresses = this.progresses.values();
        for (P progress : progresses) {
            if (progress.isCleanable()) {
                remove(progress.getId());
            }
        }
    }

}
