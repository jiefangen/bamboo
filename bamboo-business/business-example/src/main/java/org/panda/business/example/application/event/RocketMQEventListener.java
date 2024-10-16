package org.panda.business.example.application.event;

import org.panda.business.example.infrastructure.message.rocketmq.RocketMQConstants;
import org.panda.business.example.infrastructure.message.rocketmq.RocketMQConsumer;
import org.panda.tech.core.boot.ApplicationContextRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * 消费者订阅事件监听
 *
 * @author fangen
 **/
//@Component
public class RocketMQEventListener implements ApplicationContextRunner {

    @Autowired
    private RocketMQConsumer rocketMQConsumer;

    @Override
    public int getOrder() {
        return ApplicationContextRunner.super.getOrder();
    }

    @Override
    public void run(ApplicationContext context) throws Exception {
        // 开启消费者主题订阅
        rocketMQConsumer.subscribe(RocketMQConstants.OFFICIAL_MQ_TOPIC, RocketMQConstants.CONSUMER_GROUP);
    }
}
