package org.panda.business.official.infrastructure.message.rabbitmq;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.mq.rabbitmq.consumer.MessageMQConsumerSupport;

import java.nio.charset.StandardCharsets;

/**
 * 消息队列消费者
 *
 * @author fangen
 **/
//@Component
public class RabbitMQConsumer extends MessageMQConsumerSupport {

    @Override
    protected boolean consumeMessage(String queueName, byte[] message) {
        String msg = new String(message, StandardCharsets.UTF_8);
        LogUtil.info(getClass(),"{} received message：{}",queueName, msg);
        // do something
        return true;
    }

}
