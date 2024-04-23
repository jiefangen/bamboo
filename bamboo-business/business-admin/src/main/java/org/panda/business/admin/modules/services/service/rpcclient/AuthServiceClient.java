package org.panda.business.admin.modules.services.service.rpcclient;

import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.business.admin.modules.services.api.param.AddAccountParam;
import org.panda.business.admin.modules.services.api.param.UpdateAuthAccountParam;
import org.panda.business.admin.modules.services.api.vo.AuthAccountVO;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.data.model.query.Pagination;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 认证授权客户端实现
 *
 * @author fangen
 **/
@Component
@RpcClient
public class AuthServiceClient {

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
}
