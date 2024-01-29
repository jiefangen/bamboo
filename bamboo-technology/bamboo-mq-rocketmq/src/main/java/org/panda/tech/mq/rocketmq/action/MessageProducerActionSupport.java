package org.panda.tech.mq.rocketmq.action;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.panda.tech.mq.rocketmq.MessageMQProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息生产者操作支持
 */
public abstract class MessageProducerActionSupport implements MessageProducerAction {

    @Autowired
    private MessageMQProperties messageMQProperties;

    @Override
    public DefaultMQProducer buildProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(messageMQProperties.getProducerGroup());
        producer.setNamesrvAddr(messageMQProperties.getNameServer());
        return producer;
    }

}
