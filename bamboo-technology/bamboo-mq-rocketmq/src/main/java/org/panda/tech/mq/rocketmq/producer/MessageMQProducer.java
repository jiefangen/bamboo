package org.panda.tech.mq.rocketmq.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.panda.tech.mq.rocketmq.action.MessageProducerAction;

/**
 * MQ消息生产者
 **/
public interface MessageMQProducer<T> extends MessageProducerAction {

    /**
     * 普通消息同步发送
     *
     * @param topic 消息主题【必】
     * @param payload 消息数据【必】
     * @param tags 消息标签【选】
     * @param keys 消息业务关键词【选】
     */
    SendResult sendGeneralSync(String topic, T payload, String tags, String keys);

    /**
     * 普通消息异步发送
     *
     * @param topic 消息主题【必】
     * @param payload 消息数据【必】
     * @param tags 消息标签【选】
     * @param keys 消息业务关键词【选】
     * @param retryTimes 失败重试次数【选】
     */
    void sendGeneralAsync(String topic, T payload, String tags, String keys, int retryTimes);

    /**
     * 普通消息单向发送
     *
     * @param topic 消息主题【必】
     * @param payload 消息数据【必】
     * @param tags 消息标签【选】
     * @param keys 消息业务关键词【选】
     */
    void sendGeneralOneway(String topic, T payload, String tags, String keys);

    /**
     * 顺序消息发送
     *
     * @param topic 消息主题【必】
     * @param payload 消息数据【必】
     * @param tags 消息标签【选】
     * @param keys 消息业务关键词【选】
     * @param partitionId 消息分区标识【必】
     *        相同标识的消息发送到同一个队列中保证其顺序
     */
    void sendSeq(String topic, T payload, String tags, String keys, int partitionId);

    /**
     * 延迟消息发送
     *
     * @param topic 消息主题【必】
     * @param payload 消息数据【必】
     * @param tags 消息标签【选】
     * @param keys 消息业务关键词【选】
     * @param delayTimeLevel 延时等级【必】
     *        支持18个延迟等级：1-1s；2-5s；3-10s；4-30s；5-1min；6-2min；7-3min；8-4min；9-5min；10-6min；
     *                       11-7min；12-8min；13-9min；14-10min；15-20min；16-30min；17-1h；18-2h
     */
    void sendDelay(String topic, T payload, String tags, String keys, int delayTimeLevel);

    /**
     * 批量消息发送
     * 批量消息的大小不能超过1MiB（否则需要自行分割）
     *
     * @param topic 消息主题【必】
     * @param payload 消息数据【必】
     * @param tags 消息标签【选】
     * @param keys 消息业务关键词【选】
     */
    void sendBatch(String topic, T payload, String tags, String keys);

}
