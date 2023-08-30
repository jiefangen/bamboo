package org.panda.ms.notice.controller;

import io.swagger.annotations.Api;
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

    @PostMapping("/send")
    public RestfulResult send(@RequestBody @Valid EmailParam emailParam) {
        Object result = emailService.sendEmail(emailParam);
        return RestfulResult.success(result);
    }
}
