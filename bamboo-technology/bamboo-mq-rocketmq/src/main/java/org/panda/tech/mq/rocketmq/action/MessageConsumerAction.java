package org.panda.tech.mq.rocketmq.action;

/**
 * 消息消费者动作
 */
public interface MessageConsumerAction {
    /**
     * 消费者主题订阅
     *
     * @param topic 主题
     * @param tags 标签
     * @param group 消费者组
     * @param isBroadcast 是否使用广播模式
     */
    void subscribe(String topic, String tags, String group, boolean isBroadcast);

    void subscribe(String topic, String tags, String group);

    void subscribe(String topic, String group);

    void subscribe(String topic);

}
