package org.panda.business.admin.modules.services.service.manager;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.business.admin.modules.services.api.param.AddAccountParam;
import org.panda.business.admin.modules.services.api.param.UpdateAuthAccountParam;
import org.panda.business.admin.modules.services.service.rpcclient.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务管理器
 *
 * @author fangen
 **/
@Service
public class AuthServiceManager {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AuthServiceClient authServiceClient;

    public AuthServiceManager(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    public Object accountPage(AccountQueryParam queryParam) {
        try {
            return authServiceClient.accountPage(queryParam);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            return e.getMessage() == null ? null : e.getMessage();
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
            String encodedPassword = passwordEncoder.encode(password);
            accountParam.setPassword(encodedPassword);
            if (StringUtils.isEmpty(accountParam.getAccountType())) {
                accountParam.setAccountType("account");
            }
            return authServiceClient.addAccount(accountParam);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            return false;
        }
    }

    public boolean updateAccount(UpdateAuthAccountParam accountParam) {
        try {
            return authServiceClient.updateAccount(accountParam);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
            return false;
        }
    }
}
