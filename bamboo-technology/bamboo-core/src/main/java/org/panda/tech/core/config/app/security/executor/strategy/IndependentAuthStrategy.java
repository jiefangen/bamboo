package org.panda.tech.core.config.app.security.executor.strategy;

import org.panda.bamboo.common.constant.Commons;
import org.panda.tech.core.config.app.security.AuthManagerStrategy;
import org.panda.tech.core.config.app.security.executor.strategy.client.AuthServerClient;
import org.panda.tech.core.web.restful.RestfulResult;

/**
 * 独立服务认证授权验证策略
 *
 * @author fangen
 **/
public class IndependentAuthStrategy implements AuthManagerStrategy {

//    @Autowired
    private AuthServerClient authServerClient;

    @Override
    public RestfulResult<String> getAuthToken(String service, String username, String password) {
        return authServerClient.login(service, username, password);
    }

    @Override
    public RestfulResult<String> getTokenByCredentials(String secretKey, String credentials, String server) {
        return authServerClient.loginByCredentials(secretKey, credentials, server);
    }

    @Override
    public boolean verification(String token, String service) {
        RestfulResult<?> verifyResult = authServerClient.validate(token, service);
        if (verifyResult != null) {
            return Commons.RESULT_SUCCESS_CODE == verifyResult.getCode()
                    && Commons.RESULT_SUCCESS.equals(verifyResult.getMessage());
        }
        return false;
    }
}
