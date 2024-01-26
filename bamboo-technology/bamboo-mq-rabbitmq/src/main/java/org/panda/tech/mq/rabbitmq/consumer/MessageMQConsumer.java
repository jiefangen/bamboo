package org.panda.tech.mq.rabbitmq.consumer;

/**
 * 消费者动作
 **/
public interface MessageMQConsumer {

    /**
     * 消费者订阅
     * 广播模式建议自动确认
     *
     * @param queueName 队列名称
     * @param autoAck 消费自动ack确认
     * @param consumerTag 消费者标签
     */
    void subscribe(String queueName, boolean autoAck, String consumerTag);

    /**
     * 消费者订阅
     *
     * @param queueName 队列名称
     * @param consumerTag 消费者标签
     */
    void subscribe(String queueName, String consumerTag);

    /**
     * 消费者解除订阅
     *
     * @param consumerTag 消费者标签
     */
    void unsubscribe(String consumerTag);

}
