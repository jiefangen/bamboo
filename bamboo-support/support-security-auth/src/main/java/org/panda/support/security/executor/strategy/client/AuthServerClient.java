package org.panda.support.security.executor.strategy.client;

import org.panda.support.security.model.AppServiceModel;
import org.panda.support.security.model.ServiceNodeVO;
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
@RpcClient(serviceName = "auth-service")
public class AuthServerClient {

    @RpcMethod("/login")
    public RestfulResult<String> login(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       @RequestParam("service") String service) {
        return RestfulResult.failure();
    }

    @RpcMethod("/login")
    public RestfulResult<String> loginByCredentials(@RequestHeader(WebConstants.HEADER_SECRET_KEY) String secretKey,
                                                    @RequestHeader(WebConstants.HEADER_AUTH_CREDENTIALS) String credentials,
                                                    @RequestParam("service") String service) {
        return RestfulResult.failure();
    }

    @RpcMethod(value = "/app/service/access/validate", method = HttpMethod.GET)
    public RestfulResult<?> validate(@RequestHeader(WebConstants.HEADER_AUTH_JWT) String authToken,
                                     @RequestParam("service") String service) {
        return RestfulResult.failure();
    }

    @RpcMethod("/app/service/authorize")
    public RestfulResult<?> authorize(@RequestBody AppServiceModel appServiceModel) {
        return RestfulResult.failure();
    }

    @RpcMethod(value = "/api/service/getServiceNodes", method = HttpMethod.GET)
    public ServiceNodeVO getServiceNodes(@RequestParam String appName) {
        return null;
    }
}
