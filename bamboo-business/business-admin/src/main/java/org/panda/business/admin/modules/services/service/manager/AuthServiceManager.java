package org.panda.business.admin.modules.services.service.manager;

import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.business.admin.modules.services.service.rpcclient.AuthServiceClient;
import org.springframework.stereotype.Service;

/**
 * 认证服务管理器
 *
 * @author fangen
 **/
@Service
public class AuthServiceManager {

    private final AuthServiceClient authServiceClient;

    public AuthServiceManager(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    public Object accountPage(AccountQueryParam queryParam) {
        try {
            return authServiceClient.accountPage(queryParam);
        } catch (Exception e) {
            return e.getMessage() == null ? null : e.getMessage();
        }
    }

}
