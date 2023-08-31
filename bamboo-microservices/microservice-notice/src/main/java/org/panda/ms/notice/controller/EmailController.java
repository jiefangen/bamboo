package org.panda.ms.notice.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.Commons;
import org.panda.ms.notice.model.param.CustomEmailParam;
import org.panda.ms.notice.model.param.EmailParam;
import org.panda.ms.notice.service.EmailService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 邮件服务接口
 *
 * @author fangen
 */
@Api(tags = "邮件通知服务")
@RestController
@RequestMapping(value = "/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * 使用固定模版发送邮件
     */
    @PostMapping("/template/send")
    public RestfulResult templateSend(@RequestBody @Valid EmailParam emailParam) {
        Object result = emailService.sendEmail(emailParam);
        String resultStr = String.valueOf(result);
        if (!Commons.RESULT_SUCCESS.equals(resultStr)) {
            return RestfulResult.failure(resultStr);
        }
        return RestfulResult.success();
    }

    /**
     * 使用固定模版发送邮件
     */
    @PostMapping("/custom/send")
    public RestfulResult customSend(@RequestBody @Valid CustomEmailParam emailParam) {
        Object result = emailService.sendCustomEmail(emailParam);
        String resultStr = String.valueOf(result);
        if (!Commons.RESULT_SUCCESS.equals(resultStr)) {
            return RestfulResult.failure(resultStr);
        }
        return RestfulResult.success();
    }

}
