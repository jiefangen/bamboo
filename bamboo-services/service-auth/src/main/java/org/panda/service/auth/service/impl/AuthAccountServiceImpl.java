package org.panda.service.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.algorithm.HutoolSnowflake;
import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.service.auth.model.dto.AuthAccountDto;
import org.panda.service.auth.model.entity.AuthAccount;
import org.panda.service.auth.model.entity.AuthRole;
import org.panda.service.auth.model.param.AccountQueryParam;
import org.panda.service.auth.model.param.AddAccountParam;
import org.panda.service.auth.model.param.UpdateAccountParam;
import org.panda.service.auth.repository.AuthAccountMapper;
import org.panda.service.auth.service.AuthAccountService;
import org.panda.service.auth.service.AuthRoleService;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.spec.user.UsernamePassword;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.util.QueryPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
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

    @Autowired
    private AuthRoleService authRoleService;

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
        queryWrapper.orderByDesc(AuthAccount::getCreateTime);
        IPage<AuthAccount> accountPage = this.page(page, queryWrapper);
        return QueryPageHelper.convertQueryResult(accountPage);
    }

    @Override
    public boolean addAccount(AddAccountParam accountParam) {
        AuthAccount authAccount = new AuthAccount();
        String username = accountParam.getUsername();
        String password = accountParam.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return false;
        }
        authAccount.setUsername(username);
        authAccount.setPassword(password);
        // 随机生成商户账户密钥
        String secretKey = "K-" + StringUtil.randomNormalMixeds(32);
        authAccount.setSecretKey(secretKey);
        // 生成商户账户请求凭证
        UsernamePassword usernamePassword = new UsernamePassword();
        usernamePassword.setUsername(username);
        usernamePassword.setPassword(password);
        AesEncryptor aesEncryptor = new AesEncryptor();
        String credentials = aesEncryptor.encrypt(JsonUtil.toJson(usernamePassword), secretKey);
        authAccount.setCredentials(credentials);
        // 通过当前时间生成商户唯一号
        String merchantNum = TemporalUtil.formatLongNoDelimiter(Instant.now()) + HutoolSnowflake.getDistributedId();
        authAccount.setMerchantNum(merchantNum);
        String accountType = accountParam.getAccountType();
        authAccount.setAccountType(accountType);
        authAccount.setAccountRank("1");
        authAccount.setEmail(accountParam.getEmail());
        boolean isSaved = this.save(authAccount);

        // 按账户类型绑定角色关系
        if (isSaved && StringUtils.isNotEmpty(accountType)) {
            LambdaQueryWrapper<AuthRole> roleQueryWrapper = new LambdaQueryWrapper<>();
            roleQueryWrapper.eq(AuthRole::getRoleName, accountType).or()
                            .eq(AuthRole::getRoleCode, accountType.toUpperCase(Locale.ROOT));
            List<AuthRole> authRoles = authRoleService.list(roleQueryWrapper);
            if (authRoles != null && !authRoles.isEmpty()) {
                Set<Integer> roleIds = authRoles.stream().map(AuthRole::getId).collect(Collectors.toSet());
                AuthAccount account = this.getOne(Wrappers.lambdaQuery(authAccount));
                this.baseMapper.addAccountRole(account.getId(), roleIds);
            }
        }
        return isSaved;
    }

    @Override
    public boolean updateAccount(UpdateAccountParam updateAccountParam) {
        if (updateAccountParam.getId() != null) {
            AuthAccount authAccount = new AuthAccount();
            authAccount.setId(updateAccountParam.getId());
            authAccount.setEnabled(updateAccountParam.getEnabled());
            authAccount.setEmail(updateAccountParam.getEmail());
            return this.updateById(authAccount);
        }
        return false;
    }
}
