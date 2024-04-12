package org.panda.business.admin.modules.services.service.manager;

import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.business.admin.modules.services.service.rpcclient.AuthServiceClient;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证服务管理器
 *
 * @author fangen
 **/
@Service
public class AuthServiceManager {

    @Autowired
    private AuthServiceClient authServiceClient;

    public RestfulResult<?> accountPage(AccountQueryParam queryParam) {
        try {
            RestfulResult<QueryResult<?>> accountPageResult = authServiceClient.accountPage(queryParam);
            if (accountPageResult.isSuccess()) {
                QueryResult<?> queryResult = accountPageResult.getData();
                queryResult.getRecords();
            }
            return accountPageResult;
        } catch (Exception e) {
            return e.getMessage() == null ? RestfulResult.failure() : RestfulResult.failure(e.getMessage());
        }
    }

}
