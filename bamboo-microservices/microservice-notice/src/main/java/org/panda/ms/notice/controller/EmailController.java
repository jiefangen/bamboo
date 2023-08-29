package org.panda.ms.notice.controller;

import io.swagger.annotations.Api;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件服务接口
 *
 * @author fangen
 */
@Api(tags = "邮件通知服务")
@RestController
@RequestMapping(value = "/email")
public class EmailController {
    @PostMapping("/send")
    public RestfulResult send() {
        return RestfulResult.success();
    }
}
