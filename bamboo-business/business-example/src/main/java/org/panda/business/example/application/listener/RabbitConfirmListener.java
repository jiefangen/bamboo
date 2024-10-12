package org.panda.business.example.application.listener;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.mq.rabbitmq.producer.listener.AbstractConfirmListener;

//@Component
public class RabbitConfirmListener extends AbstractConfirmListener {
    @Override
    protected void manualHandleAck(long seq, boolean multiple) {
        LogUtil.info(getClass(), "Message delivery is normal, seq: {}, multiple: {}", seq, multiple);
        // do something
    }

    @Override
    protected void manualHandleNack(long seq, boolean multiple) {
        LogUtil.warn(getClass(), "Message delivery exception, seq: {}, multiple: {}", seq, multiple);
        // do something
    }
}
