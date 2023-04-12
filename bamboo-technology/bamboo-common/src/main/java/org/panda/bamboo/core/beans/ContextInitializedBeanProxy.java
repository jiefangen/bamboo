package org.panda.bamboo.core.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * 容器初始化后执行bean代理，为目标bean提供线程执行能力
 *
 * @author fangen
 */
public class ContextInitializedBeanProxy extends DelayContextInitializedBean {

    private ApplicationContext context;
    private ContextInitializedBean target;
    private long delayMillis = DEFAULT_MIN_DELAY_MILLIS;

    public void setTarget(ContextInitializedBean target) {
        Assert.isTrue(!(target instanceof ContextInitializedBeanProxy),
                "The target can not be a ContextInitializedBeanProxy");
        this.target = target;
    }

    public ContextInitializedBean getTarget() {
        return this.target;
    }

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    @Override
    protected long getDelayMillis() {
        return this.delayMillis;
    }

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        this.context = context;
        super.afterInitialized(context);
    }

    @Override
    protected void execute() {
        try {
            this.target.afterInitialized(this.context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
