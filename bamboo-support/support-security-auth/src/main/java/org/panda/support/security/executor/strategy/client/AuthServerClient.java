package org.panda.support.security.executor.strategy.client;

import org.panda.support.security.model.AppServiceModel;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 认证授权客户端实现
 *
 * @author fangen
 **/
@Component
@RpcClient
public class AuthServerClient {

    @RpcMethod("/login")
    public RestfulResult<String> login(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       @RequestParam("service") String service) {
        return RestfulResult.success();
    }

    @RpcMethod("/login")
    public RestfulResult<String> loginByCredentials(@RequestHeader(WebConstants.HEADER_SECRET_KEY) String secretKey,
                                                    @RequestHeader(WebConstants.HEADER_AUTH_CREDENTIALS) String credentials,
                                                    @RequestParam("service") String service) {
        return RestfulResult.success();
    }

    @RpcMethod(value = "/access/validate", method = HttpMethod.GET)
    public RestfulResult<?> validate(@RequestHeader(WebConstants.HEADER_AUTH_JWT) String authToken,
                                     @RequestParam("service") String service) {
        return RestfulResult.success();
    }

    @RpcMethod("/service/authorize")
    public RestfulResult<?> authorize(@RequestBody AppServiceModel appServiceModel) {
        return RestfulResult.success();
    }

}
