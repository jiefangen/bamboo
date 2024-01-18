package org.panda.tech.core.concurrent.async;

import java.io.Serializable;
import java.util.Objects;

/**
 * 任务进度
 */
public class TaskProgress<K extends Serializable> {

    private K id;
    private Long beginTime;
    private Long endTime;
    /**
     * 是否中止
     */
    private boolean stopped;

    public TaskProgress(K id) {
        this.id = id;
    }

    public K getId() {
        return this.id;
    }

    public Long getBeginTime() {
        return this.beginTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    /**
     * 通知进度中止，任务实际是否能中止、如何中止、何时中止，由具体任务内容决定
     */
    public void toStop() {
        if (!isEnded()) {
            this.stopped = true;
        }
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public void start() {
        this.beginTime = System.currentTimeMillis();
    }

    public boolean isStarted() {
        return this.beginTime != null;
    }

    public boolean isRunning() {
        return isStarted() && !isEnded();
    }

    public void end() {
        this.endTime = System.currentTimeMillis();
    }

    public boolean isEnded() {
        return this.endTime != null;
    }

    /**
     * 判断当前进度可否被清理，即在未被指定移除的情况下，可否由线程池定期清理。默认为结束时间后的10分钟后可清理，子类可覆写自定义的判断规则
     *
     * @return 可否被清理
     */
    public boolean isCleanable() {
        return this.endTime != null && System.currentTimeMillis() - this.endTime > 600000;
    }

    /**
     * @return 耗时毫秒数
     */
    public long getConsumedMilliseconds() {
        if (this.beginTime != null) {
            long endTime = Objects.requireNonNullElseGet(this.endTime, System::currentTimeMillis);
            return endTime - this.beginTime;
        }
        return 0;
    }

}
