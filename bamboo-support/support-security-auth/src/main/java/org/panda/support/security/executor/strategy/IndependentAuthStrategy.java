package org.panda.support.security.executor.strategy;

import org.panda.bamboo.common.constant.Commons;
import org.panda.support.security.AuthManagerStrategy;
import org.panda.support.security.executor.strategy.client.AuthServerClient;
import org.panda.bamboo.common.model.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 独立服务认证授权验证策略
 *
 * @author fangen
 **/
public class IndependentAuthStrategy implements AuthManagerStrategy {

    @Autowired(required = false)
    private AuthServerClient authServerClient;

    @Override
    public RestfulResult<String> getAuthToken(String username, String password, String service) {
        if (authServerClient == null) {
            return RestfulResult.failure("AuthServerClient not configured.");
        }
        return authServerClient.login(username, password, service);
    }

    @Override
    public RestfulResult<String> getTokenByCredentials(String secretKey, String credentials, String serviceName) {
        if (authServerClient == null) {
            return RestfulResult.failure("AuthServerClient not configured.");
        }
        return authServerClient.loginByCredentials(secretKey, credentials, serviceName);
    }

    @Override
    public boolean verification(String token, String service) {
        if (authServerClient != null) {
            RestfulResult<?> verifyResult = authServerClient.validate(token, service);
            if (verifyResult != null) {
                return Commons.RESULT_SUCCESS_CODE == verifyResult.getCode()
                        && Commons.RESULT_SUCCESS.equals(verifyResult.getMessage());
            }
        }
        return false;
    }
}
