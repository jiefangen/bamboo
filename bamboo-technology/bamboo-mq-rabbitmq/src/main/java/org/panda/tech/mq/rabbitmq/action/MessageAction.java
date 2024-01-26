package org.panda.tech.mq.rabbitmq.action;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 消息生产者动作
 */
public interface MessageAction {
    /**
     * 获取消息MQ连接
     */
    Connection getConnection();

    /**
     * 获取消息MQ通道
     */
    Channel getChannel();
}
