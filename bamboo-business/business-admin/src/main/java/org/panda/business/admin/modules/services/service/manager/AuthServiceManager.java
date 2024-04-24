package org.panda.business.admin.modules.services.service.manager;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.business.admin.modules.services.api.param.AccountQueryParam;
import org.panda.business.admin.modules.services.api.param.AddAccountParam;
import org.panda.business.admin.modules.services.api.param.GetAccountDetailsParam;
import org.panda.business.admin.modules.services.api.param.UpdateAuthAccountParam;
import org.panda.business.admin.modules.services.api.vo.AccountDetailsVO;
import org.panda.business.admin.modules.services.service.rpcclient.AuthServiceClient;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.spec.user.UsernamePassword;
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
            accountParam.setPassword(password);
            String encodedPassword = passwordEncoder.encode(password);
            accountParam.setEncodedPassword(encodedPassword);
            if (StringUtils.isEmpty(accountParam.getAccountType())) {
                accountParam.setAccountType("account");
            }
            boolean isAddAccount = authServiceClient.addAccount(accountParam);
            if (isAddAccount) {
                // TODO 同步创建后台商户账号

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
}
