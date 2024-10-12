package org.panda.business.example.application.listener;

import com.rabbitmq.client.AMQP;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.mq.rabbitmq.producer.listener.AbstractReturnListener;

//@Component
public class RabbitReturnListener extends AbstractReturnListener {
    @Override
    protected void manualHandleReturn(int replyCode, String replyText, String exchangeName, String routingKey,
                                      AMQP.BasicProperties basicProperties, String message) {
        LogUtil.error(getClass(), "Message distribution failed, replyCode: {}, replyText: {}, exchangeName: {},"
                + " routingKey: {}, message: {}", replyCode, replyText, exchangeName, routingKey, message);
        // do something
    }
}
