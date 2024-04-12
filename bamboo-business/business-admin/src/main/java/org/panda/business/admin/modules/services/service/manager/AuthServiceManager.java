package org.panda.business.admin.modules.services.service.manager;

import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.business.admin.modules.services.api.vo.AuthAccountVO;
import org.panda.business.admin.modules.services.service.rpcclient.AuthServiceClient;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
                QueryResult<?> queryResult = JsonUtil.json2Bean(JsonUtil.toJson(accountPageResult.getData()), QueryResult.class);
                List<AuthAccountVO> authAccountVOS = JsonUtil.json2List(JsonUtil.toJson(queryResult.getRecords()), AuthAccountVO.class);
            }
            return accountPageResult;
        } catch (Exception e) {
            return e.getMessage() == null ? RestfulResult.failure() : RestfulResult.failure(e.getMessage());
        }
    }

}
