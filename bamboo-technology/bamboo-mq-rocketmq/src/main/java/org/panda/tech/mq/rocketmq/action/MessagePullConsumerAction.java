package org.panda.tech.mq.rocketmq.action;

import java.util.List;

/**
 * 消费者主动拉取消费动作
 */
public interface MessagePullConsumerAction {
    /**
     * 主动拉取消费
     *
     * @param topic 主题
     * @param tags 标签
     * @param group 消费组
     * @return 拉取的消息
     */
    List<Object> pull(String topic, String tags, String group);

    List<Object> pull(String topic, String group);

    List<Object> pull(String topic);

}
