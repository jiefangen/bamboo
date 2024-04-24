package org.panda.business.admin.modules.services.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.business.admin.modules.services.api.param.AddAccountParam;
import org.panda.business.admin.modules.services.api.param.GetAccountDetailsParam;
import org.panda.business.admin.modules.services.api.param.UpdateAuthAccountParam;
import org.panda.business.admin.modules.services.api.vo.AccountDetailsVO;
import org.panda.business.admin.modules.services.service.manager.AuthServiceManager;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证服务控制器
 *
 * @author fangen
 **/
@Api(tags = "认证服务管理")
@RestController
@RequestMapping("/services")
public class AuthServiceController {

    @Autowired
    private AuthServiceManager authServiceManager;

    @PostMapping("/account/page")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.QUERY)
    public RestfulResult<?> accountPage(@RequestBody AccountQueryParam queryParam) {
        Object accountPageResult = authServiceManager.accountPage(queryParam);
        return accountPageResult == null ? RestfulResult.failure() : RestfulResult.success(accountPageResult);
    }

    @PostMapping("/account/add")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.ADD, intoStorage = true)
    public RestfulResult<?> addAccount(@RequestBody AddAccountParam accountParam) {
        if (authServiceManager.addAccount(accountParam)) {
            return RestfulResult.success();
        } else {
            return RestfulResult.failure();
        }
    }

    @PutMapping("/account/update")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult<?> updateAccount(@RequestBody @Valid UpdateAuthAccountParam updateAccountParam) {
        if (authServiceManager.updateAccount(updateAccountParam)) {
            return RestfulResult.success();
        }
        return RestfulResult.failure();
    }

    @PostMapping("/account/details")
    @ConfigPermission
    public RestfulResult<AccountDetailsVO> getAccountDetails(@RequestBody @Valid GetAccountDetailsParam accountDetailsParam) {
        AccountDetailsVO accountDetails = authServiceManager.getAccountDetails(accountDetailsParam);
        return RestfulResult.success(accountDetails);
    }
}
