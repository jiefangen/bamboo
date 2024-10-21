package org.panda.business.example.application.event;

import org.panda.business.example.infrastructure.message.rabbitmq.RabbitMQConstants;
import org.panda.business.example.infrastructure.message.rabbitmq.RabbitMQConsumer;
import org.panda.tech.core.boot.ApplicationContextRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 消费者订阅事件监听
 *
 * @author fangen
 **/
@Component
public class RabbitMQEventListener implements ApplicationContextRunner {

    @Autowired
    private RabbitMQConsumer rabbitMQConsumer;

    @Override
    public int getOrder() {
        return ApplicationContextRunner.super.getOrder();
    }

    @Override
    public void run(ApplicationContext context) throws Exception {
        rabbitMQConsumer.subscribe(RabbitMQConstants.QUEUE_NAME, "example.direct.consumer");
        rabbitMQConsumer.subscribe(RabbitMQConstants.DELAY_DLX_QUEUE, "example.delay.consumer");

//        rabbitMQConsumer.subscribe("topic-queue-one", "example.topic.consumer.one");
//        rabbitMQConsumer.subscribe("topic-queue-two", "example.topic.consumer.two");
//        rabbitMQConsumer.subscribe("topic-queue-three", "example.topic.consumer.three");
//
//        rabbitMQConsumer.subscribe("headers-queue-A", "example.headers.consumer.one");
//        rabbitMQConsumer.subscribe("headers-queue-B", "example.headers.consumer.two");
//        rabbitMQConsumer.subscribe("headers-queue-C", "example.headers.consumer.three");
//
//        rabbitMQConsumer.subscribe("fanout-queue-one", true,"example.fanout.consumer.one");
//        rabbitMQConsumer.subscribe("fanout-queue-two", true,"example.fanout.consumer.two");
//        rabbitMQConsumer.subscribe("fanout-queue-three", true,"example.fanout.consumer.three");
    }
}
