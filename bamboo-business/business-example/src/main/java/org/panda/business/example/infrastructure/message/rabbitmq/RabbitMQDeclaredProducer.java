package org.panda.business.example.infrastructure.message.rabbitmq;

import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.bamboo.common.model.tuple.Binary;
import org.panda.tech.mq.rabbitmq.config.ChannelDefinition;
import org.panda.tech.mq.rabbitmq.config.ExchangeEnum;
import org.panda.tech.mq.rabbitmq.config.QueueDefinition;
import org.panda.tech.mq.rabbitmq.producer.DeclaredMessageMQProducer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息队列生产者服务
 *
 * @author fangen
 **/
@Service
public class RabbitMQDeclaredProducer extends DeclaredMessageMQProducer<Object> {

    public static final String EXCHANGE_NAME = "fanout-exchange";

    public void sendFanout(Object payload) {
        super.send(EXCHANGE_NAME, null, payload);
    }

    @Override
    public String getConnectionName() {
        return RabbitMQConstants.DECLARED_PRODUCER_CONNECT;
    }

    @Override
    public String getName() {
        return RabbitMQConstants.PRODUCER_CHANNEL;
    }

    @Override
    protected ChannelDefinition getChannelDefinition() {
        ChannelDefinition definition = new ChannelDefinition();
        definition.setExchangeName(EXCHANGE_NAME);
        definition.setQueueName("fanout-queue-one");
        definition.setExchangeType(EnumValueHelper.getValue(ExchangeEnum.FANOUT));
        return definition;
    }

    @Override
    protected List<QueueDefinition> getQueueDefinition() {
        List<QueueDefinition> queues = new ArrayList<>();
        queues.add(new QueueDefinition().addQueueName("fanout-queue-two"));
        queues.add(new QueueDefinition().addQueueName("fanout-queue-three"));
        return queues;
    }

    @Override
    protected Binary<String, String> getDelayedDefinition() {
        return new Binary<>(RabbitMQConstants.DELAY_KEY, RabbitMQConstants.DELAY_ROUTING_KEY);
    }
}
