package org.panda.tech.mq.rabbitmq.config;

import com.rabbitmq.client.BuiltinExchangeType;
import lombok.Data;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.model.DomainModel;

import java.util.Map;

/**
 * 消息通道定义模型
 *
 * @author fangen
 **/
@Data
public class ChannelDefinition implements DomainModel {
    /**
     * 交换机名称
     */
    private String exchangeName;
    /**
     * 交换机类型
     * {@link ExchangeEnum}
     */
    private String exchangeType = BuiltinExchangeType.DIRECT.getType();

    /**
     * 路由绑定键
     */
    private String bindKey = Strings.EMPTY;
    /**
     * 队列名称
     */
    private String queueName;
    /**
     * 队列头信息
     */
    private Map<String, Object> queueHeaders;
    /**
     * 绑定头信息
     */
    private Map<String, Object> bindHeaders;

    /**
     * 自定义通道标签
     * 用于区分缓存不同通道连接
     */
    private String channelTag;
}
