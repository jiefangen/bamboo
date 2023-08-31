package org.panda.ms.notice.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.Commons;
import org.panda.ms.notice.model.param.CustomSmsParam;
import org.panda.ms.notice.model.param.SmsParam;
import org.panda.ms.notice.service.SmsService;
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
        Object result = smsService.sendSms(smsParam);
        String resultStr = String.valueOf(result);
        if (!Commons.RESULT_SUCCESS.equals(resultStr)) {
            return RestfulResult.failure(resultStr);
        }
        return RestfulResult.success(result);
    }

    /**
     * 使用自定义内容发送邮件
     */
    @PostMapping("/custom/send")
    public RestfulResult customSend(@RequestBody @Valid CustomSmsParam smsParam) {
        Object result = smsService.sendCustomSms(smsParam);
        String resultStr = String.valueOf(result);
        if (!Commons.RESULT_SUCCESS.equals(resultStr)) {
            return RestfulResult.failure(resultStr);
        }
        return RestfulResult.success(result);
    }

}
