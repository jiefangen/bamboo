package org.panda.tech.mq.rocketmq.producer.transaction;

import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.mq.rocketmq.action.MessageTransactionProducerActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * MQ生产事务消息抽象支持
 * <T> 消息类型
 **/
public abstract class MessageTransactionMQProducerSupport<T> extends MessageTransactionProducerActionSupport<T> {

    @Autowired(required = false)
    private ExecutorService executorService;
    @Autowired
    private TransactionListener transactionListener;

    @Override
    protected TransactionSendResult sendTransaction(TransactionMQProducer producer, String topic, T payload, String tags,
                                                    String keys, Object arg) {
        // 事务回查线程池，不设置也会默认生成
        producer.setExecutorService(getSingletonExecutorService());
        // 事务监听器
        producer.setTransactionListener(transactionListener);
        try {
            byte[] body = String.valueOf(payload).getBytes(RemotingHelper.DEFAULT_CHARSET);
            Message msg = new Message(topic, tags, keys, body);
            return producer.sendMessageInTransaction(msg, arg);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
        return null;
    }

    protected ExecutorService getSingletonExecutorService() {
        if (executorService == null) { // 上层服务未配置线程池，在此直接创建一个使用
            synchronized (this) { // 加锁创建实例，确保实例全局唯一
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(10, 20, 100,
                            TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), r -> {
                        Thread thread = new Thread(r);
                        thread.setName("client-transaction-msg-check-thread");
                        return thread;
                    });
                }
            }
        }
        return executorService;
    }

}
