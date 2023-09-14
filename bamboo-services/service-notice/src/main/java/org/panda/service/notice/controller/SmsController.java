package org.panda.service.notice.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.service.notice.core.domain.model.sms.SmsNotifyResult;
import org.panda.service.notice.model.param.CustomSmsParam;
import org.panda.service.notice.model.param.SmsParam;
import org.panda.service.notice.service.SmsService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SmsService smsService;

    @PostMapping("/template/send")
    public RestfulResult templateSend(@RequestBody @Valid SmsParam smsParam) {
        String result = smsService.sendSms(smsParam);
        if (Commons.RESULT_FAILURE.equals(result)) {
            return RestfulResult.failure();
        }
        SmsNotifyResult smsNotifyResult = JsonUtil.json2Bean(result, SmsNotifyResult.class);
        return RestfulResult.success(smsNotifyResult);
    }

    /**
     * 使用自定义内容发送邮件
     */
    @PostMapping("/custom/send")
    public RestfulResult customSend(@RequestBody @Valid CustomSmsParam smsParam) {
        String result = smsService.sendCustomSms(smsParam);
        if (Commons.RESULT_FAILURE.equals(result)) {
            return RestfulResult.failure();
        }
        SmsNotifyResult smsNotifyResult = JsonUtil.json2Bean(result, SmsNotifyResult.class);
        return RestfulResult.success(smsNotifyResult);
    }

}
