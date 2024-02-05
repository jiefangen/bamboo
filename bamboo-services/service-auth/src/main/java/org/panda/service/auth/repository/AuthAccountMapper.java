package org.panda.service.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.panda.service.auth.model.dto.AuthAccountDto;
import org.panda.service.auth.model.entity.AuthAccount;

/**
 * <p>
 * 应用认证账户 Mapper 接口
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
public interface AuthAccountMapper extends BaseMapper<AuthAccount> {

    /**
     * 通过账户属性查询账户以及关联角色集
     *
     * @param account 账户属性
     * @return 账户以及关联角色集
     */
    AuthAccountDto findAccountAndRoles(@Param("account") AuthAccount account);
}
