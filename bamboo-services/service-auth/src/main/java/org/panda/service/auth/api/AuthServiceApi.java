package org.panda.service.auth.api;

import io.swagger.annotations.Api;
import org.panda.service.auth.model.entity.AppServer;
import org.panda.service.auth.model.entity.AuthAccount;
import org.panda.service.auth.model.param.AccountQueryParam;
import org.panda.service.auth.model.param.AddAccountParam;
import org.panda.service.auth.model.param.ServiceQueryParam;
import org.panda.service.auth.model.param.UpdateAccountParam;
import org.panda.service.auth.service.AppServerService;
import org.panda.service.auth.service.AuthAccountService;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证服务API
 * 限制外部调用，只能通过内部RPC方式访问
 *
 * @author fangen
 **/
@Api(tags = "认证服务API")
@RestController
@RequestMapping("/authService")
public class AuthServiceApi {

    @Autowired
    private AuthAccountService accountService;
    @Autowired
    private AppServerService appServerService;

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
    public QueryResult<AppServer> servicePage(@RequestBody ServiceQueryParam queryParam) {
        return appServerService.getServicePage(queryParam);
    }
}
