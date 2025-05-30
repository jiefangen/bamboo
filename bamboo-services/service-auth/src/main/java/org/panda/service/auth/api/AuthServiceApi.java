package org.panda.service.auth.api;

import io.swagger.annotations.Api;
import org.panda.service.auth.model.entity.AppService;
import org.panda.service.auth.model.entity.AuthAccount;
import org.panda.service.auth.model.param.*;
import org.panda.service.auth.model.vo.PermissionInfoVO;
import org.panda.service.auth.model.vo.ServiceNodeVO;
import org.panda.service.auth.service.AppServiceService;
import org.panda.service.auth.service.AuthAccountService;
import org.panda.service.auth.service.AuthPermissionService;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证服务API
 * 限制外部调用，只能通过内部RPC方式访问
 *
 * @author fangen
 **/
@Api(tags = "【API】认证授权服务")
@RestController
@RequestMapping("/api")
public class AuthServiceApi {

    @Autowired
    private AuthAccountService accountService;
    @Autowired
    private AppServiceService appServiceService;
    @Autowired
    private AuthPermissionService permissionService;

    @PostMapping("/account/page")
    public QueryResult<AuthAccount> accountPage(@RequestBody AccountQueryParam queryParam) {
        return accountService.getAccountByPage(queryParam);
    }

    @PostMapping("/account/add")
    public boolean addAccount(@RequestBody AddAccountParam accountParam) {
        return accountService.addAccount(accountParam);
    }

    @PutMapping("/account/update")
    public boolean updateAccount(@RequestBody UpdateAccountParam updateAccountParam) {
        return accountService.updateAccount(updateAccountParam);
    }

    @PostMapping("/service/page")
    public QueryResult<AppService> servicePage(@RequestBody ServiceQueryParam queryParam) {
        return appServiceService.getServicePage(queryParam);
    }

    @PostMapping("/service/permission/info")
    public List<PermissionInfoVO> permissionInfo(@RequestBody GetPermissionParam permissionParam) {
        return permissionService.getPermissionInfo(permissionParam);
    }

    @GetMapping("/service/getServiceNodes")
    public ServiceNodeVO getServiceNodes(@RequestParam String appName) {
        return appServiceService.getServiceNodes(appName);
    }
}
