package org.panda.tech.mq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.Hashtable;
import java.util.Map;

/**
 * 消息MQ上下文
 **/
public class RabbitMQContext {

    private Connection connection = null;

    private final Map<String, Channel> channelContainer = new Hashtable<>();

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Map<String, Channel> getChannelContainer() {
        return channelContainer;
    }

    public void put(String key, Channel channel) {
        if (key != null) {
            this.channelContainer.put(key, channel);
        }
    }

    public boolean existChannel(String channelKey) {
        return this.channelContainer.containsKey(channelKey);
    }

    @SuppressWarnings("unchecked")
    public <T> T remove(String key) {
        if (key != null) {
            return (T) this.channelContainer.remove(key);
        }
        return null;
    }

    public void close() {
        try {
            // 关闭该连接下的所有通道
            for (Map.Entry<String, Channel> entry : this.channelContainer.entrySet()) {
                Channel channel = channelContainer.remove(entry.getKey());
                channel.close();
            }
            // 关闭连接
            this.connection.close();
        } catch (Exception e) {
            // do nothing
        }
    }
}
