package org.panda.tech.mq.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.core.util.CommonUtil;
import org.panda.tech.mq.rocketmq.action.MessageProducerActionSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * MQ生产消息抽象支持
 * <T> 消息类型
 **/
public abstract class MessageMQProducerSupport<T> extends MessageProducerActionSupport implements MessageMQProducer<T> {

    @Override
    public SendResult sendGeneralSync(String topic, T payload, String tags, String keys) {
        DefaultMQProducer producer = buildProducer();
        try {
            producer.start();
            byte[] body = String.valueOf(payload).getBytes(RemotingHelper.DEFAULT_CHARSET);
            Message msg = new Message(topic, tags, keys, body);
            return producer.send(msg);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        } finally {
            producer.shutdown();
        }
        return null;
    }

    /**
     * 异步发送成功回调
     *
     * @param sendResult 回调结果
     */
    protected void sendResultCallback(SendResult sendResult) {}

    @Override
    public void sendGeneralAsync(String topic, T payload, String tags, String keys, int retryTimes) {
        DefaultMQProducer producer = buildProducer();
        List<Object> payloads = CommonUtil.getPayloads(payload);
        int messageCount = payloads.size();
        CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        try {
            producer.start();
            producer.setRetryTimesWhenSendAsyncFailed(retryTimes);
            for (int i = 0; i < messageCount; i++) {
                Object message = payloads.get(i);
                byte[] body = String.valueOf(message).getBytes(RemotingHelper.DEFAULT_CHARSET);
                Message msg = new Message(topic, tags, keys, body);
                // 异步发送消息, 发送结果通过callback返回给客户端
                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        // 消息发送结果回调处理
                        sendResultCallback(sendResult);
                        LogUtil.debug(getClass(), "Async sending successful, msgId: {}", sendResult.getMsgId());
                        countDownLatch.countDown();
                    }
                    @Override
                    public void onException(Throwable e) {
                        LogUtil.error(getClass(), e);
                        countDownLatch.countDown();
                    }
                });
            }
            // 异步发送，需要等回调接口返回明确结果后才能结束逻辑，否则立即关闭Producer可能导致部分消息尚未传输成功
            countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            countDownLatch.countDown();
        } finally {
            producer.shutdown();
        }
    }

    @Override
    public void sendGeneralOneway(String topic, T payload, String tags, String keys) {
        DefaultMQProducer producer = buildProducer();
        List<Object> payloads = CommonUtil.getPayloads(payload);
        try {
            producer.start();
            for (Object message : payloads) {
                byte[] body = String.valueOf(message).getBytes(RemotingHelper.DEFAULT_CHARSET);
                Message msg = new Message(topic, tags, keys, body);
                producer.sendOneway(msg);
            }
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        } finally {
            producer.shutdown();
        }
    }

    @Override
    public void sendSeq(String topic, T payload, String tags, String keys, int partitionId) {
        DefaultMQProducer producer = buildProducer();
        List<Object> payloads = CommonUtil.getPayloads(payload);
        try {
            producer.start();
            for (int i = 0; i < payloads.size(); i++) {
                Object message = payloads.get(i);
                byte[] body = String.valueOf(message).getBytes(RemotingHelper.DEFAULT_CHARSET);
                Message msg = new Message(topic, tags, keys, body);
                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Integer id = (Integer) arg;
                        int index = id % mqs.size();
                        return mqs.get(index);
                    }
                }, partitionId);
                sendResultCallback(sendResult);
            }
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        } finally {
            producer.shutdown();
        }
    }

    @Override
    public void sendDelay(String topic, T payload, String tags, String keys, int delayTimeLevel) {
        DefaultMQProducer producer = buildProducer();
        List<Object> payloads = CommonUtil.getPayloads(payload);
        try {
            producer.start();
            for (int i = 0; i < payloads.size(); i++) {
                Object message = payloads.get(i);
                byte[] body = String.valueOf(message).getBytes(RemotingHelper.DEFAULT_CHARSET);
                Message msg = new Message(topic, tags, keys, body);
                msg.setDelayTimeLevel(delayTimeLevel);
                SendResult sendResult = producer.send(msg);
                sendResultCallback(sendResult);
            }
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        } finally {
            producer.shutdown();
        }
    }

    @Override
    public void sendBatch(String topic, T payload, String tags, String keys) {
        DefaultMQProducer producer = buildProducer();
        List<Object> payloads = CommonUtil.getPayloads(payload);
        List<Message> messages = new ArrayList<>();
        try {
            for (Object message: payloads) {
                byte[] body = String.valueOf(message).getBytes(RemotingHelper.DEFAULT_CHARSET);
                messages.add(new Message(topic, tags, keys, body));
            }
            SendResult sendResult = producer.send(messages);
            sendResultCallback(sendResult);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        } finally {
            producer.shutdown();
        }
    }

}
