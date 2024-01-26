package org.panda.tech.mq.rabbitmq.consumer;

import com.rabbitmq.client.*;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.mq.rabbitmq.action.MessageActionSupport;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * MQ消费者消息抽象支持
 **/
public abstract class MessageMQConsumerSupport extends MessageActionSupport implements MessageMQConsumer {

    // 消费者通道容器
    private final Map<String, Channel> consumerChannels = new Hashtable<>();

    @Override
    public void subscribe(String queueName, boolean autoAck, String consumerTag) {
        Channel channel;
        if (consumerChannels.containsKey(consumerTag)) {
            channel = consumerChannels.get(consumerTag);
        } else {
            channel = getChannel();
            consumerChannels.put(consumerTag, channel);
        }
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                boolean consumeResult = consumeMessage(queueName, body);
                if (!autoAck && consumeResult) {
                    // 手动消息确认，发送确认消息给RabbitMQ服务器
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        try {
            channel.basicConsume(queueName, autoAck, consumerTag, consumer);
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

    protected abstract boolean consumeMessage(String queueName, byte[] message);

    @Override
    public void subscribe(String queueName, String consumerTag) {
        subscribe(queueName, false, consumerTag);
    }

    @Override
    public void unsubscribe(String consumerTag) {
        if (consumerChannels.containsKey(consumerTag)) {
            try {
                consumerChannels.get(consumerTag).basicCancel(consumerTag);
            } catch (IOException e) {
                // do nothing
            }
        }
    }

}
