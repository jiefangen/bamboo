package org.panda.tech.mq.rocketmq.consumer.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.panda.tech.mq.rocketmq.consumer.AbstractMessageListener;

import java.util.List;

/**
 * 顺序消息监听器支持
 **/
public abstract class MessageListenerOrderlySupport extends AbstractMessageListener<ConsumeOrderlyStatus>
        implements MessageListenerOrderly {

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
        return consumeMessage(list);
    }

    protected abstract ConsumeOrderlyStatus consumeMessage(List<MessageExt> messages);

}
