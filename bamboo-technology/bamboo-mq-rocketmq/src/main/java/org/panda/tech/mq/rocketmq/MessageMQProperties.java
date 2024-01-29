package org.panda.tech.mq.rocketmq;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.core.context.SpringContextHolder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties("bamboo.message.rocketmq")
public class MessageMQProperties {

    // 默认生产者组
    public static final String DEFAULT_PRODUCER = "default-producer";
    // 默认事务消息生产者组
    public static final String DEFAULT_TRANSACTION_PRODUCER = "default-transaction-producer";
    // 默认消费者组
    public static final String DEFAULT_CONSUMER = "default-consumer";
    // 默认Pull消费者组
    public static final String DEFAULT_PULL_CONSUMER = "default-pull-consumer";

    /**
     * 服务地址
     */
    private String nameServer;
    /**
     * 生产者组
     * 释义：用于标识属于同一个逻辑生产者组的多个生产者实例，在集群应用中应有较好区分
     */
    private String producerGroup;
    /**
     * 事务消息生产者组
     */
    private List<String> transactionProducerGroups;

    /**
     * 消息消费者组
     */
    private List<String> consumerGroups;
    /**
     * 消息拉取消费者组
     */
    private List<String> pullConsumerGroups;

    public String getNameServer() {
        if (StringUtils.isEmpty(nameServer)) {
            // 尝试使用原生配置中获取
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            if (applicationContext != null) {
                Environment environment = applicationContext.getEnvironment();
                String nameServer = environment.getProperty("rocketmq.nameServer");
                if (StringUtils.isEmpty(nameServer)) {
                    nameServer = environment.getProperty("rocketmq.name-server");
                }
                setNameServer(nameServer);
            }
        }
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getProducerGroup() {
        if (StringUtils.isEmpty(producerGroup)) {
            // 未配置则使用默认生产者组
            setProducerGroup(DEFAULT_PRODUCER);
        }
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public List<String> getTransactionProducerGroups() {
        if (transactionProducerGroups == null) {
            transactionProducerGroups = new ArrayList<>();
        }
        if (!transactionProducerGroups.contains(DEFAULT_TRANSACTION_PRODUCER)) {
            transactionProducerGroups.add(DEFAULT_TRANSACTION_PRODUCER);
        }
        return transactionProducerGroups;
    }

    public void setTransactionProducerGroups(List<String> transactionProducerGroups) {
        this.transactionProducerGroups = transactionProducerGroups;
    }

    public List<String> getConsumerGroups() {
        if (consumerGroups == null) {
            consumerGroups = new ArrayList<>();
        }
        if (!consumerGroups.contains(DEFAULT_CONSUMER)) {
            consumerGroups.add(DEFAULT_CONSUMER);
        }
        return consumerGroups;
    }

    public void setConsumerGroups(List<String> consumerGroups) {
        this.consumerGroups = consumerGroups;
    }

    public List<String> getPullConsumerGroups() {
        if (pullConsumerGroups == null) {
            pullConsumerGroups = new ArrayList<>();
        }
        if (!pullConsumerGroups.contains(DEFAULT_PULL_CONSUMER)) {
            pullConsumerGroups.add(DEFAULT_PULL_CONSUMER);
        }
        return pullConsumerGroups;
    }

    public void setPullConsumerGroups(List<String> pullConsumerGroups) {
        this.pullConsumerGroups = pullConsumerGroups;
    }
}
