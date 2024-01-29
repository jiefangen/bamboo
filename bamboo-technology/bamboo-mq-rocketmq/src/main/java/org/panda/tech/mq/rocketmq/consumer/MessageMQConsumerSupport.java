package org.panda.tech.mq.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.mq.rocketmq.action.MessageConsumerActionSupport;

/**
 * MQ消费者消息抽象支持
 * 交由业务使用方配置初始化，避免消息生产方初始化不需要的该配置
 **/
public abstract class MessageMQConsumerSupport extends MessageConsumerActionSupport {

    protected static final int DEFAULT_MAX_RECONSUME_TIMES = 16;
    protected static final long DEFAULT_RETRY_INTERVAL = 5000L;

    @Override
    protected void subscribe(DefaultMQPushConsumer consumer, String topic, String tags) {
        try {
            // 订阅主题，指定tag过滤条件
            consumer.subscribe(topic, tags);
            // 优先从普通消息中获取消费监听器
            MessageListenerConcurrently messageListener = messageListenerContainer.get(topic);
            if (messageListener == null) {
                // 顺序消息监听器
                MessageListenerOrderly messageListenerOrderly = messageListenerOrderlyContainer.get(topic);
                if (messageListenerOrderly == null) {
                    throw new BusinessException(topic + " consumer topic listener is not configured");
                }
                // 最大重试次数
                consumer.setMaxReconsumeTimes(getMaxReconsumeTimes());
                // 重试间隔时间
                consumer.setSuspendCurrentQueueTimeMillis(getRetryInterval());
                consumer.registerMessageListener(messageListenerOrderly);
            } else {
                // 注册回调接口来处理从Broker中收到的消息
                consumer.registerMessageListener(messageListener);
            }
            consumer.start();
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
    }

    /**
     * 最大重试次数
     * 并发消费中有默认值不建议自定义
     * 顺序消费无最大限制，可自定义配置
     */
    protected int getMaxReconsumeTimes() {
        return DEFAULT_MAX_RECONSUME_TIMES;
    }
    /**
     * 重试间隔，只在顺序消费中起作用
     */
    protected long getRetryInterval() {
        return DEFAULT_RETRY_INTERVAL;
    }

}
