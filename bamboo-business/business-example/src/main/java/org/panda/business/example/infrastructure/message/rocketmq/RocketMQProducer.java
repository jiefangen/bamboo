package org.panda.business.example.infrastructure.message.rocketmq;

import org.apache.rocketmq.client.producer.SendResult;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.mq.rocketmq.producer.MessageMQProducerSupport;

/**
 * 消息队列生产者服务
 *
 * @author fangen
 **/
//@Service
public class RocketMQProducer extends MessageMQProducerSupport<Object> {

    @Override
    protected void sendResultCallback(SendResult sendResult) {
        LogUtil.info(getClass(), "Send result callback result: {}", sendResult.getMsgId());
    }
}
