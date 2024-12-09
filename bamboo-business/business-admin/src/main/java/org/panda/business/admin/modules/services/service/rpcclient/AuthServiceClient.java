package org.panda.business.admin.modules.services.service.rpcclient;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.business.admin.modules.services.api.param.*;
import org.panda.business.admin.modules.services.api.vo.AppServerVO;
import org.panda.business.admin.modules.services.api.vo.AuthAccountVO;
import org.panda.business.admin.modules.services.api.vo.PermissionInfoVO;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.data.model.query.Pagination;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证授权客户端实现
 *
 * @author fangen
 **/
@Component
@RpcClient
public class AuthServiceClient {

    @RpcMethod(value = "/home", method = HttpMethod.GET)
    public String home() {
        return Strings.EMPTY;
    }

    @RpcMethod(value = "/authService/account/page", subTypes = AuthAccountVO.class)
    public QueryResult<AuthAccountVO> accountPage(@RequestBody AccountQueryParam queryParam) {
        return QueryResult.empty(new Pagination());
    }

    @RpcMethod("/authService/account/add")
    public boolean addAccount(@RequestBody AddAccountParam addAccountParam) {
        return false;
    }

    @RpcMethod(value = "/authService/account/update", method = HttpMethod.PUT)
    public boolean updateAccount(@RequestBody UpdateAuthAccountParam authAccountParam) {
        return false;
    }

    @RpcMethod(value = "/authService/service/page", subTypes = AppServerVO.class)
    public QueryResult<AppServerVO> servicePage(@RequestBody ServiceQueryParam queryParam) {
        return QueryResult.empty(new Pagination());
    }

    @RpcMethod(value = "/authService/service/permission/info", subTypes = PermissionInfoVO.class)
    public List<PermissionInfoVO> permissionInfo(@RequestBody GetPermissionParam permissionParam) {
        return new ArrayList<>();
    }
}
