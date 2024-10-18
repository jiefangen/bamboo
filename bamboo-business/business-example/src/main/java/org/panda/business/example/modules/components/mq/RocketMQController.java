package org.panda.business.example.modules.components.mq;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.Api;
import org.apache.rocketmq.client.producer.SendResult;
import org.panda.bamboo.common.util.lang.UUIDUtil;
import org.panda.business.example.infrastructure.message.rocketmq.RocketMQConstants;
import org.panda.business.example.infrastructure.message.rocketmq.RocketMQProducer;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 消息队列消息生产器
 *
 * @author fangen
 **/
@Api(tags = "RocketMQ消息生产器")
//@RestController
@RequestMapping(value = "/produce")
public class RocketMQController {

    @Autowired
    private RocketMQProducer rocketMQProducer;

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

}
