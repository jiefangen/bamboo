package org.panda.business.example.modules.components.mq;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.Api;
import org.apache.rocketmq.client.producer.SendResult;
import org.panda.bamboo.common.util.lang.UUIDUtil;
import org.panda.business.example.infrastructure.message.rabbitmq.RabbitMQConstants;
import org.panda.business.example.infrastructure.message.rabbitmq.RabbitMQDeclaredProducer;
import org.panda.business.example.infrastructure.message.rabbitmq.RabbitMQProducer;
import org.panda.business.example.infrastructure.message.rocketmq.RocketMQConstants;
import org.panda.business.example.infrastructure.message.rocketmq.RocketMQProducer;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消息队列消息生产器
 *
 * @author fangen
 **/
@Api(tags = "消息队列消息生产器")
//@RestController
@RequestMapping(value = "/produce")
public class ProducerMQController {

    @Autowired
    private RocketMQProducer rocketMQProducer;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    private RabbitMQDeclaredProducer rabbitMQDeclaredProducer;

    @GetMapping("/sendGeneralSync")
    public RestfulResult<?> sendGeneralSync() {
        JSONObject msgJson = new JSONObject();
        msgJson.put("message", "Official say: Hello RocketMQ!");
        SendResult sendResult = rocketMQProducer.sendGeneralSync(RocketMQConstants.OFFICIAL_MQ_TOPIC, msgJson,
                "sync-msg", UUIDUtil.randomUUID32());
        if (sendResult == null) {
            return RestfulResult.failure();
        }
        return RestfulResult.success(sendResult);
    }

    @GetMapping("/sendDirect")
    public RestfulResult<?> sendDirect() {
        List<String> messages = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String message = "Official say: Hello RabbitMQ! Seq: ";
            messages.add(message + i);
        }
        String message = "Official say: Hello RabbitMQ!";
        rabbitMQProducer.sendDirect(RabbitMQConstants.ROUTING_KEY, message);
        return RestfulResult.success();
    }

    @GetMapping("/sendDelayed")
    public RestfulResult<?> sendDelayed() {
        String message = "Official delayed say: Hello RabbitMQ!";
        String exchangeName = RabbitMQConstants.DELAY_KEY + "-ttl-exchange";
        rabbitMQDeclaredProducer.sendDelayed(exchangeName, 3000L,
                RabbitMQConstants.DELAY_ROUTING_KEY, message);
        return RestfulResult.success();
    }

    @GetMapping("/sendTopic")
    public RestfulResult<?> sendTopic(@RequestParam String routingKey) {
        String message = "Official topic say: Hello RabbitMQ!";
        rabbitMQProducer.sendTopic(routingKey, message);
        return RestfulResult.success();
    }

    @GetMapping("/sendHeaders")
    public RestfulResult<?> sendHeaders(@RequestParam Map<String, Object> format, @RequestParam Map<String, Object> type) {
        String message = "Official headers say: Hello RabbitMQ!";
        format.putAll(type);
        rabbitMQProducer.sendHeaders(format, message);
        return RestfulResult.success();
    }

    @GetMapping("/sendFanout")
    public RestfulResult<?> sendFanout() {
        String message = "Official fanout say: Hello RabbitMQ!";
        rabbitMQDeclaredProducer.sendFanout(message);
        return RestfulResult.success();
    }

    @GetMapping("/sendDirectTemp")
    public RestfulResult<?> sendDirectTemp() {
        String message = "Official temporary say: Hello RabbitMQ!";
        rabbitMQProducer.sendDirectTemp(message);
        return RestfulResult.success();
    }

}
