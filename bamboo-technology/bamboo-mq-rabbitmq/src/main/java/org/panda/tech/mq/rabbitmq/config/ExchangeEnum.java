package org.panda.tech.mq.rabbitmq.config;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 交换机类型枚举
 */
public enum ExchangeEnum {

    /**
     * 根据路由键完全匹配的原则进行路由
     */
    @Caption("直连交换机")
    @EnumValue("direct")
    DIRECT,

    /**
     * 根据路由键的模式匹配进行路由，支持星号（*）和井号（#）通配符
     */
    @Caption("主题交换机")
    @EnumValue("topic")
    TOPIC,

    /**
     * 根据消息头中的键值对进行路由，忽略路由键。
     */
    @Caption("头交换机")
    @EnumValue("headers")
    HEADERS,

    /**
     * 广播消息到所有绑定的队列，忽略路由键
     */
    @Caption("扇型交换机")
    @EnumValue("fanout")
    FANOUT

}
