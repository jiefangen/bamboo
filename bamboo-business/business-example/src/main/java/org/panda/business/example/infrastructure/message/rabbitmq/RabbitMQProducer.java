package org.panda.business.example.infrastructure.message.rabbitmq;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.mq.rabbitmq.config.ChannelDefinition;
import org.panda.tech.mq.rabbitmq.config.QueueDefinition;
import org.panda.tech.mq.rabbitmq.producer.MessageMQProducerSupport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息队列生产者服务
 *
 * @author fangen
 **/
@Service
public class RabbitMQProducer extends MessageMQProducerSupport<Object> {

    @Override
    public String getConnectionName() {
        return RabbitMQConstants.PRODUCER_CONNECT;
    }

    public void sendDirect(String routingKey, Object payload) {
        List<String> payloadList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            payloadList.add(payload + " serial number：" + i);
        }
        // 提前组装好批量发送信息，其性能最佳。
        super.sendDirect(getDirectChannelDefinition(), routingKey, null, payloadList, true);
    }

    public void batchSendDirect(String routingKey, Object payload) {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            new Thread(() -> {
                LogUtil.info(getClass(), "Running serial number：{}", finalI);
                String payloadStr = payload + " serial number：" + finalI;
                // 异步多线程场景下，由于通道（channel）不是线程安全的，故连接通道不能复用。
                super.sendDirect(getDirectChannelDefinition(), routingKey, null, payloadStr, false);
            }).start();
        }
    }

    private ChannelDefinition getDirectChannelDefinition() {
        ChannelDefinition definition = new ChannelDefinition();
        definition.setExchangeName(RabbitMQConstants.EXCHANGE_NAME);
        definition.setQueueName(RabbitMQConstants.QUEUE_NAME);
        definition.setBindKey(RabbitMQConstants.ROUTING_KEY);
        definition.setChannelTag(RabbitMQConstants.PRODUCER_CHANNEL);
        return definition;
    }

    public void sendTopic(String routingKey, Object payload) {
        List<QueueDefinition> queues = new ArrayList<>();
        queues.add(new QueueDefinition().addQueueName("topic-queue-one").addBindKey("*.orange.*"));
        queues.add(new QueueDefinition().addQueueName("topic-queue-two").addBindKey("*.*.rabbit"));
        queues.add(new QueueDefinition().addQueueName("topic-queue-three").addBindKey("lazy.#"));
        super.sendTopic(getTopicChannelDefinition(), queues, routingKey, null, payload);
    }

    private ChannelDefinition getTopicChannelDefinition() {
        ChannelDefinition definition = new ChannelDefinition();
        definition.setExchangeName("topic-exchange");
        definition.setChannelTag(RabbitMQConstants.PRODUCER_CHANNEL);
        return definition;
    }

    public void sendHeaders(Map<String, Object> sendHeaders, Object payload) {
        List<QueueDefinition> queues = new ArrayList<>();
        Map<String, Object> headers = new HashMap<>();
        headers.put("format", "pdf");
        headers.put("type", "report");
        headers.put("x-match", "all"); // 头交换机必须匹配类型
        queues.add(new QueueDefinition().addQueueName("headers-queue-B").addBindHeaders(headers));

        Map<String, Object> headersC = new HashMap<>();
        headersC.put("format", "zip");
        headersC.put("type", "report");
        headersC.put("x-match", "all");
        queues.add(new QueueDefinition().addQueueName("headers-queue-C").addBindHeaders(headersC));
        super.sendHeaders(getHeadersChannelDefinition(), sendHeaders, queues, null, payload);
    }

    private ChannelDefinition getHeadersChannelDefinition() {
        ChannelDefinition definition = new ChannelDefinition();
        definition.setExchangeName("headers-exchange");
        definition.setQueueName("headers-queue-A");
        Map<String, Object> headers = new HashMap<>();
        headers.put("format", "pdf");
        headers.put("type", "log");
        headers.put("x-match", "any"); // 头交换机必须匹配类型
        definition.setBindHeaders(headers);
        definition.setChannelTag(RabbitMQConstants.PRODUCER_CHANNEL);
        return definition;
    }

    public void sendDirectTemp(Object payload) {
        String routingKey = "temp-direct-key";
        ChannelDefinition definition = new ChannelDefinition();
        definition.setExchangeName("temp-direct-exchange");
        definition.setBindKey(routingKey);
        super.sendDirect(definition, routingKey, payload);
    }

}
