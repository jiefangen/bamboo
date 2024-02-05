package org.panda.service.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.panda.service.auth.model.dto.AuthAccountDto;
import org.panda.service.auth.model.entity.AuthAccount;
import org.panda.service.auth.model.entity.AuthRole;
import org.panda.service.auth.repository.AuthAccountMapper;
import org.panda.service.auth.service.AuthAccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 应用认证账户 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
@Service
public class AuthAccountServiceImpl extends ServiceImpl<AuthAccountMapper, AuthAccount> implements AuthAccountService {

    @Override
    public AuthAccountDto getAccountAndRoles(String username) {
        AuthAccount accountParam = new AuthAccount();
        accountParam.setUsername(username);
        AuthAccountDto authAccountDto = this.baseMapper.findAccountAndRoles(accountParam);
        if (authAccountDto == null) {
            return null;
        }
        List<AuthRole> roles = authAccountDto.getRoles();
        if(CollectionUtils.isNotEmpty(roles)) {
            Set<String> roleCodes = roles.stream().map(role -> role.getRoleCode()).collect(Collectors.toSet());
            authAccountDto.setRoleCodes(roleCodes);
        }
        return authAccountDto;
    }
}
