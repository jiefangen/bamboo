package org.panda.business.admin.modules.services.service.manager;

import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.business.admin.modules.services.api.vo.AuthAccountVO;
import org.panda.business.admin.modules.services.service.rpcclient.AuthServiceClient;
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

    public Object accountPage(AccountQueryParam queryParam) {
        try {
            QueryResult<AuthAccountVO> accountPageResult = authServiceClient.accountPage(queryParam);
            return accountPageResult;
        } catch (Exception e) {
            return e.getMessage() == null ? null : e.getMessage();
        }
    }

}
