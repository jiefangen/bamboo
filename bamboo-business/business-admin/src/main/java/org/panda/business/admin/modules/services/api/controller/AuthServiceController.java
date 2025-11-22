package org.panda.business.admin.modules.services.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.modules.services.api.param.*;
import org.panda.business.admin.modules.services.api.vo.AccountDetailsVO;
import org.panda.business.admin.modules.services.api.vo.AppServerVO;
import org.panda.business.admin.modules.services.api.vo.AuthAccountVO;
import org.panda.business.admin.modules.services.api.vo.PermissionInfoVO;
import org.panda.business.admin.modules.services.service.manager.AuthServiceManager;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.spec.log.annotation.WebOperationLog;
import org.panda.bamboo.common.model.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/auth/home")
    @ConfigAnonymous
    @WebOperationLog(actionType = ActionType.OTHER)
    public RestfulResult<Object> authHome() {
        return authServiceManager.authHome();
    }

    @PostMapping("/account/page")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.QUERY)
    public RestfulResult<QueryResult<AuthAccountVO>> getAccountPage(@RequestBody AccountQueryParam queryParam) {
        QueryResult<AuthAccountVO> accountPageResult = authServiceManager.accountPage(queryParam);
        return accountPageResult == null ? RestfulResult.failure() : RestfulResult.success(accountPageResult);
    }

    @PostMapping("/account/add")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.ADD, intoStorage = true)
    public RestfulResult<Boolean> addAccount(@RequestBody AddAccountParam accountParam) {
        if (authServiceManager.addAccount(accountParam)) {
            return RestfulResult.success();
        } else {
            return RestfulResult.failure();
        }
    }

    @PutMapping("/account/update")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult<Boolean> updateAccount(@RequestBody @Valid UpdateAuthAccountParam updateAccountParam) {
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

    @PostMapping("/service/page")
    @ConfigPermission
    @WebOperationLog(actionType = ActionType.QUERY)
    public RestfulResult<QueryResult<AppServerVO>> getServicePage(@RequestBody ServiceQueryParam queryParam) {
        QueryResult<AppServerVO> servicePageResult = authServiceManager.servicePage(queryParam);
        return servicePageResult == null ? RestfulResult.failure() : RestfulResult.success(servicePageResult);
    }

    @PostMapping("/service/permission/info")
    @ConfigPermission
    public RestfulResult<List<PermissionInfoVO>> getPermissionInfo(@RequestBody GetPermissionParam permissionParam) {
        List<PermissionInfoVO> servicePageResult = authServiceManager.getPermissionInfo(permissionParam);
        return servicePageResult == null ? RestfulResult.failure() : RestfulResult.success(servicePageResult);
    }
}
