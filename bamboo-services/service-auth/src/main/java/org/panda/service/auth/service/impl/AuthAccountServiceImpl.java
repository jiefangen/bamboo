package org.panda.service.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.service.auth.model.dto.AuthAccountDto;
import org.panda.service.auth.model.entity.AuthAccount;
import org.panda.service.auth.model.entity.AuthRole;
import org.panda.service.auth.model.param.AccountQueryParam;
import org.panda.service.auth.repository.AuthAccountMapper;
import org.panda.service.auth.service.AuthAccountService;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.util.QueryPageHelper;
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

    @Override
    public QueryResult<AuthAccount> getAccountByPage(AccountQueryParam queryParam) {
        Page<AuthAccount> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<AuthAccount> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryParam.getKeyword())) {
            queryWrapper.like(AuthAccount::getUsername, queryParam.getKeyword()).or()
                        .like(AuthAccount::getMerchantNum, queryParam.getKeyword());
        }
        if (StringUtils.isNotBlank(queryParam.getUsername())) {
            queryWrapper.like(AuthAccount::getUsername, queryParam.getUsername());
        }
        if (StringUtils.isNotBlank(queryParam.getMerchantNum())) {
            queryWrapper.eq(AuthAccount::getMerchantNum, queryParam.getMerchantNum());
        }
        queryWrapper.orderByAsc(AuthAccount::getCreateTime);
        IPage<AuthAccount> accountPage = this.page(page, queryWrapper);
        return QueryPageHelper.convertQueryResult(accountPage);
    }
}
