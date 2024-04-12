package org.panda.business.admin.modules.services.service.rpcclient;

import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.core.web.restful.RestfulResult;
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

    @RpcMethod(value = "/service/account/page", subType = QueryResult.class)
    public RestfulResult<QueryResult<?>> accountPage(@RequestBody AccountQueryParam queryParam) {
        return RestfulResult.success();
    }

}
