package org.panda.business.example.application.event;

import org.panda.business.example.infrastructure.message.rabbitmq.RabbitMQConstants;
import org.panda.business.example.infrastructure.message.rabbitmq.RabbitMQConsumer;
import org.panda.tech.core.boot.ApplicationContextRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * 消费者订阅事件监听
 *
 * @author fangen
 **/
//@Component
public class RabbitMQEventListener implements ApplicationContextRunner {

    @Autowired
    private RabbitMQConsumer rabbitMQConsumer;

    @Override
    public int getOrder() {
        return ApplicationContextRunner.super.getOrder();
    }

    @Override
    public void run(ApplicationContext context) throws Exception {
        rabbitMQConsumer.subscribe(RabbitMQConstants.QUEUE_NAME, "official.direct.consumer");
        rabbitMQConsumer.subscribe(RabbitMQConstants.DELAY_DLX_QUEUE, "official.delay.consumer");

        rabbitMQConsumer.subscribe("topic-queue-one", "official.topic.consumer.one");
        rabbitMQConsumer.subscribe("topic-queue-two", "official.topic.consumer.two");
        rabbitMQConsumer.subscribe("topic-queue-three", "official.topic.consumer.three");

        rabbitMQConsumer.subscribe("headers-queue-A", "official.headers.consumer.one");
        rabbitMQConsumer.subscribe("headers-queue-B", "official.headers.consumer.two");
        rabbitMQConsumer.subscribe("headers-queue-C", "official.headers.consumer.three");

        rabbitMQConsumer.subscribe("fanout-queue-one", true,"official.fanout.consumer.one");
        rabbitMQConsumer.subscribe("fanout-queue-two", true,"official.fanout.consumer.two");
        rabbitMQConsumer.subscribe("fanout-queue-three", true,"official.fanout.consumer.three");
    }
}
