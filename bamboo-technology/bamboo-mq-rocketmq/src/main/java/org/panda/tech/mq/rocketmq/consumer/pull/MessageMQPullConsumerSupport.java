package org.panda.tech.mq.rocketmq.consumer.pull;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.mq.rocketmq.action.MessagePullConsumerActionSupport;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * MQ消费者消息主动拉取抽象支持
 **/
public abstract class MessageMQPullConsumerSupport extends MessagePullConsumerActionSupport {

    // 消费者状态
    private final AtomicBoolean consumerState = new AtomicBoolean();

    @Override
    protected List<Object> pull(DefaultLitePullConsumer pullConsumer, String topic, String tags) {
        try {
            pullConsumer.subscribe(topic, tags);
            if (!consumerState.get()) {
                // 不额外设置默认是自动提交位点
                // 同一个消费组下的多个LitePullConsumer会负载均衡消费，与PushConsumer一致
                if (getPullMaxSize() != null) {
                    pullConsumer.setPullBatchSize(getPullMaxSize());
                }
                pullConsumer.start();
                consumerState.set(true);
            }
            List<MessageExt> messages = pullConsumer.poll();
            consumeMessage(messages);
            // 数据提取封装
            List<Object> body = new ArrayList<>();
            for (MessageExt message : messages) {
                String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                body.add(msg);
            }
            return body;
        } catch (MQClientException e) {
            LogUtil.error(getClass(), e);
        }
        return null;
    }

    protected abstract void consumeMessage(List<MessageExt> messages);

    /**
     * 每次拉取最大消息数量
     */
    protected Integer getPullMaxSize() {
        return null;
    }

}
