package org.panda.service.auth.controller;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.service.auth.infrastructure.security.app.AppServiceModel;
import org.panda.service.auth.model.entity.AuthAccount;
import org.panda.service.auth.model.param.AccountQueryParam;
import org.panda.service.auth.service.AppServerService;
import org.panda.service.auth.service.AuthAccountService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用服务授权控制器
 *
 * @author fangen
 **/
@Api(tags = "应用服务控制器")
@RestController
@RequestMapping("/service")
public class AppServiceController {

    @Autowired
    private AppServerService appServerService;

    @Autowired
    private AuthAccountService accountService;

    @PostMapping("/authorize")
    @ConfigAnonymous
    public RestfulResult<?> authorize(@RequestBody AppServiceModel appServiceModel) {
        if (appServiceModel == null || StringUtils.isEmpty(appServiceModel.getAppName())) {
            return RestfulResult.failure();
        }
        String result = appServerService.initServicePermission(appServiceModel);
        if (Commons.RESULT_SUCCESS.equals(result)) {
            return RestfulResult.success();
        } else {
            return RestfulResult.failure(result);
        }
    }

    @PostMapping("/account/page") // 限制外部调用，只能通过内部RPC方式访问
    public RestfulResult<?> accountPage(@RequestBody AccountQueryParam queryParam) {
        QueryResult<AuthAccount> accountQuery = accountService.getAccountByPage(queryParam);
        return RestfulResult.success(accountQuery);
    }

}
