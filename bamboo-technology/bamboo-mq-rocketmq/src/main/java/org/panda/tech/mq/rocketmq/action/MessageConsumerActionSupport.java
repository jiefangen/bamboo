package org.panda.tech.mq.rocketmq.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.mq.rocketmq.MessageMQProperties;
import org.panda.tech.mq.rocketmq.consumer.listener.MessageListenerOrderlySupport;
import org.panda.tech.mq.rocketmq.consumer.listener.MessageListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息消费者操作支持
 */
public abstract class MessageConsumerActionSupport implements MessageConsumerAction, ContextInitializedBean {

    // 消息消费者组容器
    private final Map<String, DefaultMQPushConsumer> consumerContainer = new HashMap<>();
    // 普通消息监听器
    protected final Map<String, MessageListenerSupport> messageListenerContainer = new HashMap<>();
    // 顺序消息监听器
    protected final Map<String, MessageListenerOrderlySupport> messageListenerOrderlyContainer = new HashMap<>();

    @Autowired
    private MessageMQProperties messageMQProperties;

    /**
     * 初始化Push消息MQ消费者
     */
    protected DefaultMQPushConsumer initMQPushConsumer(String consumerGroup) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(messageMQProperties.getNameServer());
        return consumer;
    }

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        messageMQProperties.getConsumerGroups().forEach(consumerGroup -> {
            if (!consumerContainer.containsKey(consumerGroup)) {
                DefaultMQPushConsumer consumer = initMQPushConsumer(consumerGroup);
                this.consumerContainer.put(consumerGroup, consumer);
            }
        });
        context.getBeansOfType(MessageListenerSupport.class).forEach((id, messageListener) -> {
            String topic = messageListener.getTopic();
            if (StringUtils.isNotEmpty(topic) && !messageListenerContainer.containsKey(topic)) {
                messageListenerContainer.put(messageListener.getTopic(), messageListener);
            }
        });
        context.getBeansOfType(MessageListenerOrderlySupport.class).forEach((id, messageListener) -> {
            String topic = messageListener.getTopic();
            if (StringUtils.isNotEmpty(topic) && !messageListenerOrderlyContainer.containsKey(topic)) {
                messageListenerOrderlyContainer.put(messageListener.getTopic(), messageListener);
            }
        });
    }

    @Override
    public void subscribe(String topic, String tags, String group, boolean isBroadcast) {
        if (StringUtils.isEmpty(group)) {
            group = MessageMQProperties.DEFAULT_CONSUMER;
        }
        DefaultMQPushConsumer consumer = consumerContainer.get(group);
        if (consumer == null) {
            throw new BusinessException("Consumer group " + group + " is not configured");
        }
        if (isBroadcast) {
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }
        this.subscribe(consumer, topic, tags);
    }

    @Override
    public void subscribe(String topic, String tags, String group) {
        subscribe(topic, tags, group, false);
    }

    @Override
    public void subscribe(String topic, String group) {
        subscribe(topic, Strings.ASTERISK, group);
    }

    @Override
    public void subscribe(String topic) {
        subscribe(topic, Strings.ASTERISK, null);
    }

    protected void shutdown(String group) {
        DefaultMQPushConsumer consumer = this.consumerContainer.get(group);
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    protected void shutdown() {
        DefaultMQPushConsumer consumer = this.consumerContainer.get(MessageMQProperties.DEFAULT_CONSUMER);
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    protected abstract void subscribe(DefaultMQPushConsumer consumer, String topic, String tags);

}
