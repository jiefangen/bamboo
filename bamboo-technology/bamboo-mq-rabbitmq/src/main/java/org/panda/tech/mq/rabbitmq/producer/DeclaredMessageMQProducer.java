package org.panda.tech.mq.rabbitmq.producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.model.tuple.Binary;
import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.tech.core.spec.Named;
import org.panda.tech.mq.rabbitmq.config.ChannelDefinition;
import org.panda.tech.mq.rabbitmq.config.QueueDefinition;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 可预先声明队列和绑定关系的消息生产者
 **/
public abstract class DeclaredMessageMQProducer<T> extends MessageMQProducerSupport<T> implements Named,
        ContextInitializedBean {

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        if (getChannelDefinition() != null) {
            Channel channel = channelDeclare(getChannelDefinition(), getQueueDefinition(),true);
            closeChannel(channel);
        }
        Binary<String, String> delayedBinary = getDelayedDefinition();
        if (delayedBinary != null) {
            delayedDeclare(delayedBinary.getLeft(), delayedBinary.getRight());
        }
    }

    protected abstract ChannelDefinition getChannelDefinition();

    protected List<QueueDefinition> getQueueDefinition() {
        return null;
    }

    /**
     * 发送
     *
     * @param exchangeName 交换机名称
     * @param routingKey 路由键
     * @param properties 参数配置
     * @param payload 消息
     * @param channelReuse 通道复用
     */
    public void send(String exchangeName, String routingKey, AMQP.BasicProperties properties, T payload,
                     boolean channelReuse) {
        String channelKey = buildChannelKey(exchangeName, Strings.EMPTY, routingKey, Strings.EMPTY, getName());
        Channel channel;
        if (channelReuse) {
            if (rabbitMQContext.existChannel(channelKey)) {
                channel = rabbitMQContext.getChannelContainer().get(channelKey);
            } else {
                channel = super.getChannel();
                // 存入消息上下文连接通道容器
                rabbitMQContext.put(channelKey, channel);
            }
        } else { // 通道不复用，每次都需创建新的通道
            channel = super.getChannel();
        }
        super.send(channel, exchangeName, routingKey, properties, payload, channelReuse);
    }

    /**
     * 发送
     *
     * @param exchangeName 交换机名称
     * @param routingKey 路由键
     * @param payload 消息
     */
    public void send(String exchangeName, String routingKey, T payload) {
        send(exchangeName, routingKey, null, payload, true);
    }

    /**
     * 发送延时消息
     *
     * @param exchangeName 延时消息业务标识
     * @param expiration 延时时间，单位毫秒
     * @param routingKey 路由键
     * @param payload 消息
     */
    public void sendDelayed(String exchangeName, long expiration, String routingKey, T payload) {
        Map<String, Object> propertiesHeaders = new HashMap<>();
        propertiesHeaders.put("x-delay", expiration);
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .headers(propertiesHeaders)
                .expiration(String.valueOf(expiration))
                .build();
        send(exchangeName, routingKey, properties, payload, true);
    }

    /**
     * 获取延时队列定义
     *
     * @return 二元结构：左元素为延时标识，右元素为路由绑定键
     */
    protected Binary<String, String> getDelayedDefinition() {
        return null;
    }

    private void delayedDeclare(String keyName, String routingKey) {
        // 声明延时交换机和队列
        ChannelDefinition definition = new ChannelDefinition();
        definition.setExchangeName(keyName + "-ttl-exchange");
        definition.setQueueName(keyName + "-ttl-queue");
        definition.setBindKey(routingKey);
        definition.setChannelTag("ttl-delayed");
        Map<String, Object> headers = new HashMap<>(3);
        headers.put("x-dead-letter-exchange", keyName + DLX_EXCHANGE_SUFFIX); // 绑定死信交换机
        headers.put("x-dead-letter-routing-key", keyName + DLX_QUEUE_SUFFIX); // 绑定当前队列的死信路由key
//        headers.put("x-message-ttl", expiration); // 声明队列的TTL
        definition.setQueueHeaders(headers);
        // 延时通道
        Channel ttlChannel = channelDeclare(definition, true);
        closeChannel(ttlChannel);

        // 声明死信交换机和队列
        ChannelDefinition dlxDefinition = new ChannelDefinition();
        dlxDefinition.setChannelTag("dlx-delayed");
        dlxDefinition.setExchangeName(keyName + DLX_EXCHANGE_SUFFIX);
        dlxDefinition.setQueueName(keyName + DLX_QUEUE_SUFFIX);
        dlxDefinition.setBindKey(keyName + DLX_QUEUE_SUFFIX);
        Channel dlxChannel = channelDeclare(dlxDefinition, true);
        closeChannel(dlxChannel);
    }

}
