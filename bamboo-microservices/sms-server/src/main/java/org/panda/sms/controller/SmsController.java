package org.panda.sms.controller;

import org.panda.sms.domain.params.SmsParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信服务接口
 *
 * @author fangen
 * @since 2022/8/21
 */
@RestController
@RequestMapping(value = "sms")
public class SmsController {
    @PostMapping("/send")
    public String send(@RequestBody SmsParam smsParam) {
        return "Send SMS";
    }
}
