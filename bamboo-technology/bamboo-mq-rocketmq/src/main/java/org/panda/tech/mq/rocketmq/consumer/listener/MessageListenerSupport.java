package org.panda.tech.mq.rocketmq.consumer.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.panda.tech.mq.rocketmq.consumer.AbstractMessageListener;

import java.util.List;

/**
 * 普通消息监听器支持
 **/
public abstract class MessageListenerSupport extends AbstractMessageListener<ConsumeConcurrentlyStatus>
        implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeContext) {
        return consumeMessage(list);
    }

    protected abstract ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages);

}
