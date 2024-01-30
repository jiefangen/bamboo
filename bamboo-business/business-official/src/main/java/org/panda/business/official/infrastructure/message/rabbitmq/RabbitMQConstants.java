package org.panda.business.official.infrastructure.message.rabbitmq;

import org.panda.tech.mq.rabbitmq.action.MessageActionSupport;

/**
 * 消息队列规范常量
 */
public class RabbitMQConstants {
    /**
     * 直连交换机名称
     */
    public static final String EXCHANGE_NAME = "direct-exchange";
    /**
     * 直连队列名
     */
    public static final String QUEUE_NAME = "direct-queue";
    /**
     * 直连绑定路由键
     */
    public static final String ROUTING_KEY = "direct-key";

    /**
     * 默认生产者通道标签
     */
    public static final String PRODUCER_CHANNEL = "official.producer";

    /**
     * 延时键
     */
    public static final String DELAY_KEY = "delayed";
    /**
     * 延时队列绑定路由键
     */
    public static final String DELAY_ROUTING_KEY = "delayed-key";
    /**
     * 延时（死信）消费队列名称
     */
    public static final String DELAY_DLX_QUEUE = DELAY_KEY + MessageActionSupport.DLX_QUEUE_SUFFIX;
}
