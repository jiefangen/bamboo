package org.panda.tech.mq.rocketmq.producer.transaction;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.panda.bamboo.common.util.LogUtil;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象事务消息监听器
 **/
public abstract class AbstractTransactionListener implements TransactionListener {

    protected ConcurrentHashMap<String, LocalTransactionState> localTrans = new ConcurrentHashMap<>();

    // 半事务消息发送成功后，执行本地事务的方法
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
        LocalTransactionState localTransactionState = executeTransaction(message, arg);
        if (localTransactionState != null) {
            localTrans.put(message.getTransactionId(), localTransactionState);
            return localTransactionState;
        }
        return LocalTransactionState.UNKNOW;
    }

    // 未收到本地事务执行结果确认时，回查事务状态
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        String transactionId = messageExt.getTransactionId();
        LocalTransactionState localTransactionState = localTrans.get(transactionId);
        LogUtil.warn(getClass(), "Check local transaction, transactionId: {}, state: {}", transactionId,
                localTransactionState);
        if (localTransactionState != null && localTransactionState != LocalTransactionState.UNKNOW) {
            localTrans.remove(transactionId);
            return localTransactionState;
        }
        return LocalTransactionState.UNKNOW;
    }

    protected abstract LocalTransactionState executeTransaction(Message message, Object arg);

}
