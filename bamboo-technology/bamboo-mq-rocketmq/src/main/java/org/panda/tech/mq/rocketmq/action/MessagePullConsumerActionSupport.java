package org.panda.tech.mq.rocketmq.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.mq.rocketmq.MessageMQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息消费者拉取操作支持
 */
public abstract class MessagePullConsumerActionSupport implements MessagePullConsumerAction, ContextInitializedBean {

    // 消息消费者组容器
    private final Map<String, DefaultLitePullConsumer> pullConsumerContainer = new HashMap<>();

    @Autowired
    private MessageMQProperties messageMQProperties;

    /**
     * 初始化Pull消息MQ消费者
     */
    protected DefaultLitePullConsumer initMQPullConsumer(String consumerGroup) {
        DefaultLitePullConsumer pullConsumer = new DefaultLitePullConsumer(consumerGroup);
        pullConsumer.setNamesrvAddr(messageMQProperties.getNameServer());
        return pullConsumer;
    }

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        messageMQProperties.getPullConsumerGroups().forEach(pullConsumerGroup -> {
            if (!pullConsumerContainer.containsKey(pullConsumerGroup)) {
                DefaultLitePullConsumer pullConsumer = initMQPullConsumer(pullConsumerGroup);
                this.pullConsumerContainer.put(pullConsumerGroup, pullConsumer);
            }
        });
    }

    @Override
    public List<Object> pull(String topic, String tags, String group) {
        if (StringUtils.isEmpty(group)) {
            group = MessageMQProperties.DEFAULT_PULL_CONSUMER;
        }
        DefaultLitePullConsumer pullConsumer = pullConsumerContainer.get(group);
        if (pullConsumer == null) {
            throw new BusinessException("Consumer group " + group + " is not configured");
        }
        return pull(pullConsumer, topic, tags);
    }

    @Override
    public List<Object> pull(String topic, String group) {
        return pull(topic, Strings.ASTERISK, group);
    }


    @Override
    public List<Object> pull(String topic) {
        return pull(topic, null);
    }

    protected abstract List<Object> pull(DefaultLitePullConsumer pullConsumer, String topic, String tags);

}
