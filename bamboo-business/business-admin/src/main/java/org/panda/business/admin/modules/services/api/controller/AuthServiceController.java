package org.panda.business.admin.modules.services.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.business.admin.modules.services.service.manager.AuthServiceManager;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证服务控制器
 *
 * @author fangen
 **/
@Api(tags = "认证服务管理")
@RestController
@RequestMapping("/auth/service")
public class AuthServiceController {

    @Autowired
    private AuthServiceManager authServiceManager;

    @PostMapping("/account/page")
    @ConfigAnonymous
    @WebOperationLog(actionType = ActionType.QUERY)
    public RestfulResult<?> accountPage(@RequestBody AccountQueryParam queryParam) {
        RestfulResult<?> accountPageResult = authServiceManager.accountPage(queryParam);
        return accountPageResult;
    }

}
