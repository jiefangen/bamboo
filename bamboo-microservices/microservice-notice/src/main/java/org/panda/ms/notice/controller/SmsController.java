package org.panda.ms.notice.controller;

import io.swagger.annotations.Api;
import org.panda.ms.notice.model.param.SmsParam;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 短信服务接口
 *
 * @author fangen
 */
@Api(tags = "短信通知服务")
@RestController
@RequestMapping(value = "/sms")
public class SmsController {
    @PostMapping("/send")
    public RestfulResult send(@RequestBody @Valid SmsParam smsParam) {
        return RestfulResult.success(smsParam);
    }
}
