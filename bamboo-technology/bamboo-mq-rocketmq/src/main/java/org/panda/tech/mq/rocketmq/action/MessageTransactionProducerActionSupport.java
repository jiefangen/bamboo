package org.panda.tech.mq.rocketmq.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.mq.rocketmq.MessageMQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 事务消息生产者操作支持
 */
public abstract class MessageTransactionProducerActionSupport<T> implements MessageTransactionProducerAction<T>,
        ContextInitializedBean {

    protected static final int STATE_SHUTDOWN = 0;
    protected static final int STATE_START = 1;

    // 事务消息生产者组
    private final Map<String, TransactionMQProducer> producerContainer = new HashMap<>();
    // 事务消息生产者状态：0-关闭；1-开启
    protected final Map<TransactionMQProducer, Integer> producerStates = new HashMap<>();

    @Autowired
    private MessageMQProperties messageMQProperties;

    private TransactionMQProducer buildTransactionMQProducer(String transactionProducerGroup) {
        TransactionMQProducer producer = buildProducer();
        producer.setProducerGroup(transactionProducerGroup);
        producer.setNamesrvAddr(messageMQProperties.getNameServer());
        return producer;
    }

    @Override
    public TransactionMQProducer buildProducer() {
        return new TransactionMQProducer();
    }

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        messageMQProperties.getTransactionProducerGroups().forEach(transactionProducerGroup -> {
            if (!producerContainer.containsKey(transactionProducerGroup)) {
                TransactionMQProducer producer = buildTransactionMQProducer(transactionProducerGroup);
                this.producerContainer.put(transactionProducerGroup, producer);
                // 初始化状态为关闭，发送时才开启
                this.producerStates.put(producer, STATE_SHUTDOWN);
            }
        });
    }

    @Override
    public TransactionSendResult send(String topic, T payload, String tags, String keys, Object arg, String group) {
        if (StringUtils.isEmpty(group)) {
            group = MessageMQProperties.DEFAULT_TRANSACTION_PRODUCER;
        }
        TransactionMQProducer producer = this.producerContainer.get(group);
        if (producer == null) {
            throw new BusinessException("Producer group " + group + " is not configured");
        }
        // 从状态组中判断生产者是否开启
        if (STATE_SHUTDOWN == this.producerStates.get(producer)) {
            synchronized (this) {
                if (STATE_SHUTDOWN == this.producerStates.get(producer)) {
                    try {
                        producer.start();
                    } catch (MQClientException e) {
                        LogUtil.error(getClass(), e);
                    }
                    this.producerStates.put(producer, STATE_START);
                }
            }
        }
        return sendTransaction(producer, topic, payload, tags, keys, arg);
    }

    @Override
    public TransactionSendResult send(String topic, T payload, String tags, String keys, Object arg) {
        return send(topic, payload, tags, keys, arg, null);
    }

    protected void shutdown(String group) {
        TransactionMQProducer producer = this.producerContainer.get(group);
        if (producer != null) {
            producer.shutdown();
            this.producerStates.put(producer, STATE_SHUTDOWN);
        }
    }

    protected void shutdown() {
        TransactionMQProducer producer = this.producerContainer.get(MessageMQProperties.DEFAULT_TRANSACTION_PRODUCER);
        if (producer != null) {
            producer.shutdown();
            this.producerStates.put(producer, STATE_SHUTDOWN);
        }
    }

    protected abstract TransactionSendResult sendTransaction(TransactionMQProducer transactionMQProducer,
                                                             String topic, T payload, String tags, String keys, Object arg);

}
