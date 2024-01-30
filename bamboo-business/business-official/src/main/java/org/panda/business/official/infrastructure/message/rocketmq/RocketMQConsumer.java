package org.panda.business.official.infrastructure.message.rocketmq;

import org.panda.tech.mq.rocketmq.consumer.MessageMQConsumerSupport;

/**
 * 消息队列消费者
 *
 * @author fangen
 **/
//@Component
public class RocketMQConsumer extends MessageMQConsumerSupport {

    @Override
    protected int getMaxReconsumeTimes() {
        return super.getMaxReconsumeTimes();
    }

    @Override
    protected long getRetryInterval() {
        return super.getRetryInterval();
    }

}
