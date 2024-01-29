package org.panda.tech.mq.rocketmq.consumer;

import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 抽象消费者消息监听器
 **/
public abstract class AbstractMessageListener<T> {

    public abstract String getTopic();

    protected abstract T consumeMessage(List<MessageExt> messages);

}
