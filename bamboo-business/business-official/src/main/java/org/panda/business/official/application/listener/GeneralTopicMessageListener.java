package org.panda.business.official.application.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.official.infrastructure.message.rocketmq.RocketMQConstants;
import org.panda.tech.mq.rocketmq.consumer.listener.MessageListenerSupport;

import java.nio.charset.StandardCharsets;
import java.util.List;

//@Component
public class GeneralTopicMessageListener extends MessageListenerSupport {

    @Override
    public String getTopic() {
        return RocketMQConstants.OFFICIAL_MQ_TOPIC;
    }

    @Override
    protected ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages) {
        for (MessageExt message : messages) {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            LogUtil.info(getClass(),"{} received message：{}", getTopic(), msg);
            // do something
        }
        // 返回RECONSUME_LATER表示消费失败，一段时间后再重新消费
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
