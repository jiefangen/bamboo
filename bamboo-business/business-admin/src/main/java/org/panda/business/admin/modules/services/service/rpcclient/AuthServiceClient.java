package org.panda.business.admin.modules.services.service.rpcclient;

import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.business.admin.modules.services.api.param.AddAccountParam;
import org.panda.business.admin.modules.services.api.vo.AuthAccountVO;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.data.model.query.Pagination;
import org.panda.tech.data.model.query.QueryResult;
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

    @RpcMethod(value = "/service/account/page", subTypes = AuthAccountVO.class)
    public QueryResult<AuthAccountVO> accountPage(@RequestBody AccountQueryParam queryParam) {
        return QueryResult.empty(new Pagination());
    }

    @RpcMethod("/service/account/add")
    public boolean accountAdd(@RequestBody AddAccountParam queryParam) {
        return false;
    }
}
