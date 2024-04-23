package org.panda.service.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.service.auth.model.dto.AuthAccountDto;
import org.panda.service.auth.model.entity.AuthAccount;
import org.panda.service.auth.model.param.AccountQueryParam;
import org.panda.service.auth.model.param.AddAccountParam;
import org.panda.service.auth.model.param.UpdateAccountParam;
import org.panda.tech.data.model.query.QueryResult;

/**
 * <p>
 * 应用认证账户 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
public interface AuthAccountService extends IService<AuthAccount> {

    AuthAccountDto getAccountAndRoles(String username);

    QueryResult<AuthAccount> getAccountByPage(AccountQueryParam queryParam);

    boolean addAccount(AddAccountParam accountParam);

    boolean updateAccount(UpdateAccountParam updateAccountParam);
}
