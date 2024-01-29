package org.panda.tech.mq.rocketmq.action;

import org.apache.rocketmq.client.producer.TransactionSendResult;

/**
 * 事务消息生产者动作
 */
public interface MessageTransactionProducerAction<T> extends MessageProducerAction {

    TransactionSendResult send(String topic, T payload, String tags, String keys, Object arg, String group);

    TransactionSendResult send(String topic, T payload, String tags, String keys, Object arg);

}
