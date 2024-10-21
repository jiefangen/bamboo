package org.panda.business.example.modules.components.mq;

import io.swagger.annotations.Api;
import org.panda.business.example.infrastructure.message.rabbitmq.RabbitMQConstants;
import org.panda.business.example.infrastructure.message.rabbitmq.RabbitMQDeclaredProducer;
import org.panda.business.example.infrastructure.message.rabbitmq.RabbitMQProducer;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 消息队列消息生产器
 *
 * @author fangen
 **/
@Api(tags = "RabbitMQ消息生产器")
@RestController
@RequestMapping(value = "/rabbitmq/produce")
public class RabbitMQController {

    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    private RabbitMQDeclaredProducer rabbitMQDeclaredProducer;

    @GetMapping("/sendDirect")
    public RestfulResult<?> sendDirect() {
        String message = "Example direct say: Hello RabbitMQ!";
        rabbitMQProducer.sendDirect(RabbitMQConstants.ROUTING_KEY, message);
        return RestfulResult.success();
    }

    @GetMapping("/batchSendDirect")
    public RestfulResult<?> batchSendDirect() {
        String message = "Example batch direct say: Hello RabbitMQ!";
        rabbitMQProducer.batchSendDirect(RabbitMQConstants.ROUTING_KEY, message);
        return RestfulResult.success();
    }

    @GetMapping("/sendDelayed")
    public RestfulResult<?> sendDelayed() {
        String message = "Example delayed say: Hello RabbitMQ!";
        String exchangeName = RabbitMQConstants.DELAY_KEY + "-ttl-exchange";
        rabbitMQDeclaredProducer.sendDelayed(exchangeName, 3000L,
                RabbitMQConstants.DELAY_ROUTING_KEY, message);
        return RestfulResult.success();
    }

    @GetMapping("/sendTopic")
    public RestfulResult<?> sendTopic(@RequestParam String routingKey) {
        String message = "Example topic say: Hello RabbitMQ!";
        rabbitMQProducer.sendTopic(routingKey, message);
        return RestfulResult.success();
    }

    @GetMapping("/sendHeaders")
    public RestfulResult<?> sendHeaders(@RequestParam Map<String, Object> format, @RequestParam Map<String, Object> type) {
        String message = "Example headers say: Hello RabbitMQ!";
        format.putAll(type);
        rabbitMQProducer.sendHeaders(format, message);
        return RestfulResult.success();
    }

    @GetMapping("/sendFanout")
    public RestfulResult<?> sendFanout() {
        String message = "Example fanout say: Hello RabbitMQ!";
        rabbitMQDeclaredProducer.sendFanout(message);
        return RestfulResult.success();
    }

    @GetMapping("/sendDirectTemp")
    public RestfulResult<?> sendDirectTemp() {
        String message = "Example temporary say: Hello RabbitMQ!";
        rabbitMQProducer.sendDirectTemp(message);
        return RestfulResult.success();
    }

}
