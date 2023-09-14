package org.panda.service.notice.core.domain.single.email.send;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件发送进度
 */
public class EmailSendProgress {

    private int total;
    private Collection<Serializable> successes = new ArrayList<>();
    private Map<Serializable, Exception> failureExceptionMap = new HashMap<>();
    /**
     * 是否中途终止
     */
    private boolean stop;

    /**
     * @param total 需发送的邮件总数
     */
    public EmailSendProgress(final int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }

    public Collection<Serializable> getSuccesses() {
        return this.successes;
    }

    public int getSuccessCount() {
        return this.successes.size();
    }

    /**
     * 添加成功对象
     *
     * @param success 添加的成功对象
     */
    public void addSuccess(final Serializable success) {
        this.successes.add(success);
    }

    public Map<Serializable, Exception> getFailureExceptionMap() {
        return this.failureExceptionMap;
    }

    public int getFailureCount() {
        return this.failureExceptionMap.size();
    }

    /**
     * 判断邮件是否全部发送成功
     *
     * @return true if 邮件全部发送成功, otherwise false
     */
    public boolean isAllSuccess() {
        return getFailureCount() == 0;
    }

    public Iterable<Serializable> getFailures() {
        return this.failureExceptionMap.keySet();
    }

    public Exception getFailureException(final Serializable failure) {
        return this.failureExceptionMap.get(failure);
    }

    /**
     * 添加指定失败对象及其原因异常
     *
     * @param failure 失败对象
     * @param e       原因异常
     */
    public void addFailure(final Serializable failure, final Exception e) {
        this.failureExceptionMap.put(failure, e);
    }

    /**
     * 判断邮件是否全部发送失败
     *
     * @return true if 邮件全部发送失败, otherwise false
     */
    public boolean isAllFailure() {
        return getSuccessCount() == 0;
    }

    /**
     * @return 是否中途终止
     */
    public boolean isStop() {
        return this.stop;
    }

    /**
     * 终止发送
     */
    public void stop() {
        this.stop = true;
    }
}
