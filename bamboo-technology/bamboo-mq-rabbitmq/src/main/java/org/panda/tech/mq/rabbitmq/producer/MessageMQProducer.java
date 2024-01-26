package org.panda.tech.mq.rabbitmq.producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.panda.tech.mq.rabbitmq.config.ChannelDefinition;
import org.panda.tech.mq.rabbitmq.config.QueueDefinition;

import java.util.List;
import java.util.Map;

/**
 * MQ消息生产者
 **/
public interface MessageMQProducer<T> {

    /**
     * 消息发送
     * 多线程场景需关闭通道复用
     *
     * @param channel 通道
     * @param exchangeName 交换机名称
     * @param routingKey 路由键
     * @param properties 参数
     * @param payload 消息
     * @param channelReuse 通道复用
     */
    void send(Channel channel, String exchangeName, String routingKey, AMQP.BasicProperties properties, T payload,
              boolean channelReuse);

    /**
     * 直连模式发送
     *
     * @param definition 通道定义
     * @param routingKey 路由键
     * @param properties 消息参数
     * @param payload 消息
     * @param channelReuse 通道复用
     */
    void sendDirect(ChannelDefinition definition, String routingKey, AMQP.BasicProperties properties, T payload,
                    boolean channelReuse);

    /**
     * 直连模式发送
     *
     * @param definition 通道定义
     * @param routingKey 路由键
     * @param payload 消息
     */
    void sendDirect(ChannelDefinition definition, String routingKey, T payload);

    /**
     * 主题模式发送
     *
     * @param definition 通道定义
     * @param routingKey 路由键
     * @param properties 消息参数
     * @param payload 消息
     */
    void sendTopic(ChannelDefinition definition, List<QueueDefinition> queues, String routingKey,
                   AMQP.BasicProperties properties, T payload);

    /**
     * 消息头模式发送
     *
     * @param definition 通道定义
     * @param headers 消息头
     * @param queues 绑定队列集
     * @param properties 消息参数
     * @param payload 消息
     */
    void sendHeaders(ChannelDefinition definition, Map<String, Object> headers, List<QueueDefinition> queues,
                     AMQP.BasicProperties properties, T payload);

    /**
     * 广播模式发送
     *
     * @param definition 通道定义
     * @param queues 绑定队列集
     * @param properties 消息参数
     * @param payload 消息
     */
    void sendFanout(ChannelDefinition definition, List<QueueDefinition> queues, AMQP.BasicProperties properties,
                    T payload);

}
