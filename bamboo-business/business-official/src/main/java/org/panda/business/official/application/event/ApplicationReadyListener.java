package org.panda.business.official.application.event;

import org.panda.business.official.infrastructure.message.rabbitmq.RabbitMQConstants;
import org.panda.business.official.infrastructure.message.rabbitmq.RabbitMQConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * 监听事件，应用程序启动完成后执行初始化任务
 *
 * @author fangen
 **/
//@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

//    @Autowired
//    private RocketMQConsumer rocketMQConsumer;
    @Autowired
    private RabbitMQConsumer rabbitMQConsumer;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 开启消费者主题订阅
//        rocketMQConsumer.subscribe(RocketMQConstants.OFFICIAL_MQ_TOPIC, RocketMQConstants.CONSUMER_GROUP);

        rabbitMQConsumer.subscribe(RabbitMQConstants.QUEUE_NAME, "official.direct.consumer");
        rabbitMQConsumer.subscribe(RabbitMQConstants.DELAY_DLX_QUEUE, "official.delay.consumer");

//        rabbitMQConsumer.subscribe("topic-queue-one", "official.topic.consumer.one");
//        rabbitMQConsumer.subscribe("topic-queue-two", "official.topic.consumer.two");
//        rabbitMQConsumer.subscribe("topic-queue-three", "official.topic.consumer.three");

//        rabbitMQConsumer.subscribe("headers-queue-A", "official.headers.consumer.one");
//        rabbitMQConsumer.subscribe("headers-queue-B", "official.headers.consumer.two");
//        rabbitMQConsumer.subscribe("headers-queue-C", "official.headers.consumer.three");

        rabbitMQConsumer.subscribe("fanout-queue-one", "official.fanout.consumer.one");
        rabbitMQConsumer.subscribe("fanout-queue-two", "official.fanout.consumer.two");
        rabbitMQConsumer.subscribe("fanout-queue-three", "official.fanout.consumer.three");
    }

}
