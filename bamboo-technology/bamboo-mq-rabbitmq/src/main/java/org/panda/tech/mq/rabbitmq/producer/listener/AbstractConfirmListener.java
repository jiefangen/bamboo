package org.panda.tech.mq.rabbitmq.producer.listener;

import com.rabbitmq.client.ConfirmListener;

import java.io.IOException;

/**
 * 抽象消息发送确认监听器
 **/
public abstract class AbstractConfirmListener implements ConfirmListener {

    /**
     * 消息投递正常（到达RabbitMQ服务器）
     */
    @Override
    public void handleAck(long seq, boolean multiple) throws IOException {
        manualHandleAck(seq, multiple);
    }

    /**
     * 消息投递异常（未到达RabbitMQ服务器）
     */
    @Override
    public void handleNack(long seq, boolean multiple) throws IOException {
        manualHandleNack(seq, multiple);
    }

    /**
     * 手动处理消息投递成功
     *
     * @param seq 消息序号
     * @param multiple 是否批量确认
     */
    protected abstract void manualHandleAck(long seq, boolean multiple);

    /**
     * 手动处理消息投递异常
     *
     * @param seq 消息序号
     * @param multiple 是否批量确认
     */
    protected abstract void manualHandleNack(long seq, boolean multiple);

}
