package org.panda.business.admin.modules.services.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.business.admin.modules.services.api.param.*;
import org.panda.business.admin.modules.services.api.vo.AccountDetailsVO;
import org.panda.business.admin.modules.services.api.vo.AppServerVO;
import org.panda.business.admin.modules.services.api.vo.AuthAccountVO;
import org.panda.business.admin.modules.services.api.vo.PermissionInfoVO;
import org.panda.business.admin.modules.services.service.rpcclient.AuthServiceApiClient;
import org.panda.business.admin.modules.system.api.param.AddUserParam;
import org.panda.business.admin.modules.system.service.SysRoleService;
import org.panda.business.admin.modules.system.service.SysUserService;
import org.panda.business.admin.modules.system.service.entity.SysRole;
import org.panda.business.admin.modules.system.service.entity.SysUser;
import org.panda.business.admin.modules.system.service.repository.SysUserMapper;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.spec.user.UsernamePassword;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证服务管理器
 *
 * @author fangen
 **/
@Service
public class AuthServiceManager {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysUserMapper userMapper;

    private final AuthServiceApiClient authServiceClient;

    public AuthServiceManager(AuthServiceApiClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    public RestfulResult<Object> authHome() {
        try {
            return authServiceClient.home();
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            return null;
        }
    }

    public QueryResult<AuthAccountVO> accountPage(AccountQueryParam queryParam) {
        try {
            return authServiceClient.accountPage(queryParam);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            return null;
        }
    }

    public boolean addAccount(AddAccountParam accountParam) {
        try {
            // 账户名或密码为空则自动随机生成
            if (StringUtils.isEmpty(accountParam.getUsername())) {
                accountParam.setUsername(StringUtil.randomLetters(13, Strings.EMPTY));
            }
            String password = accountParam.getPassword();
            if (StringUtils.isEmpty(password)) {
                password = StringUtil.randomNormalMixeds(16);
            }
            accountParam.setPassword(password);
            String encodedPassword = passwordEncoder.encode(password);
            accountParam.setEncodedPassword(encodedPassword);
            if (StringUtils.isEmpty(accountParam.getAccountType())) {
                accountParam.setAccountType("account");
            }
            boolean isAddAccount = authServiceClient.addAccount(accountParam);

            if (isAddAccount) { // 同步创建后台商户账号
                AddUserParam userParam = new AddUserParam();
                userParam.setUsername(accountParam.getUsername());
                userParam.setPassword(password);
                userParam.setNickname(accountParam.getUsername());
                userParam.setEmail(accountParam.getEmail());
                String accountType = accountParam.getAccountType();
                userParam.setUserType(accountParam.getAccountType());
                String addUserResult = userService.addUser(userParam);
                if (Commons.RESULT_SUCCESS.equals(addUserResult)) {
                    // 根据账户类型自动绑定相应的角色
                    LambdaQueryWrapper<SysRole> roleQueryWrapper = new LambdaQueryWrapper<>();
                    roleQueryWrapper.eq(SysRole::getRoleName, accountType).or()
                            .eq(SysRole::getRoleCode, accountType.toUpperCase(Locale.ROOT));
                    List<SysRole> authRoles = roleService.list(roleQueryWrapper);
                    if (authRoles != null && !authRoles.isEmpty()) {
                        Set<Integer> roleIds = authRoles.stream().map(SysRole::getId).collect(Collectors.toSet());
                        LambdaQueryWrapper<SysUser> userQueryWrapper = new LambdaQueryWrapper<>();
                        userQueryWrapper.eq(SysUser::getUsername, userParam.getUsername());
                        SysUser sysUser = userService.getOne(userQueryWrapper);
                        userMapper.updateUserRole(sysUser.getId(), roleIds);
                    }
                }
            }
            return isAddAccount;
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
        return false;
    }

    public boolean updateAccount(UpdateAuthAccountParam accountParam) {
        try {
            return authServiceClient.updateAccount(accountParam);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            return false;
        }
    }

    public AccountDetailsVO getAccountDetails(GetAccountDetailsParam accountDetailsParam) {
        AccountDetailsVO accountDetails = new AccountDetailsVO();
        accountDetails.setMerchantNum(accountDetailsParam.getMerchantNum());
        String secretKey = accountDetailsParam.getSecretKey();
        accountDetails.setSecretKey(secretKey);
        String credentials = accountDetailsParam.getCredentials();
        accountDetails.setCredentials(credentials);
        // 账户密码解密
        AesEncryptor aesEncryptor = new AesEncryptor();
        String decryptSource = aesEncryptor.decrypt(credentials, secretKey);
        UsernamePassword usernamePassword = JsonUtil.json2Bean(decryptSource, UsernamePassword.class);
        accountDetails.setUsername(usernamePassword.getUsername());
        accountDetails.setPassword(usernamePassword.getPassword());
        return accountDetails;
    }

    public QueryResult<AppServerVO> servicePage(ServiceQueryParam queryParam) {
        try {
            return authServiceClient.servicePage(queryParam);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            return null;
        }
    }

    public List<PermissionInfoVO> getPermissionInfo(GetPermissionParam permissionParam) {
        try {
            // 服务ID用作权限资源来源
            permissionParam.setResourcesId(permissionParam.getId());
            return authServiceClient.permissionInfo(permissionParam);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            return null;
        }
    }
}
