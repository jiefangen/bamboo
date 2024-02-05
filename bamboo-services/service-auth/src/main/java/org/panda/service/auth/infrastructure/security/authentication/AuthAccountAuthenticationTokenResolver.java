package org.panda.service.auth.infrastructure.security.authentication;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.service.auth.model.entity.AuthAccount;
import org.panda.service.auth.service.AuthAccountService;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.exception.business.param.RequiredParamException;
import org.panda.tech.core.spec.user.UsernamePassword;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.security.authentication.DefaultAuthenticationToken;
import org.panda.tech.security.web.authentication.DefaultAuthenticationTokenResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录认证解决起扩展账号凭证解析
 **/
public class AuthAccountAuthenticationTokenResolver extends DefaultAuthenticationTokenResolver {

    @Autowired
    private AuthAccountService accountService;

    public AuthAccountAuthenticationTokenResolver(String loginMode) {
        super(loginMode);
    }

    @Override
    public DefaultAuthenticationToken resolveAuthenticationToken(HttpServletRequest request) {
        String credentials = WebHttpUtil.getHeader(request, WebConstants.HEADER_AUTH_CREDENTIALS);
        if (StringUtils.isNotEmpty(credentials)) {
            String secretKey = WebHttpUtil.getHeader(request, WebConstants.HEADER_SECRET_KEY);
            if (StringUtils.isEmpty(secretKey)) {
                // 上游服务未传入则从数据库中查询密钥
                LambdaQueryWrapper<AuthAccount> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(AuthAccount::getCredentials, credentials);
                AuthAccount authAccount = accountService.getOne(queryWrapper, false);
                if (authAccount != null) {
                    secretKey = authAccount.getSecretKey();
                } else {
                    throw new RequiredParamException("Account secret-key missing");
                }
            }
            // 账户凭证解密
            AesEncryptor aesEncryptor = new AesEncryptor();
            String decryptSource = aesEncryptor.decrypt(credentials, secretKey);
            if (StringUtils.isEmpty(decryptSource)) {
                throw new BusinessException("Account credentials decryption exception");
            }
            UsernamePassword usernamePassword = JsonUtil.json2Bean(decryptSource, UsernamePassword.class);
            if (usernamePassword != null) {
                request.setAttribute(DEFAULT_PARAMETER_USERNAME, usernamePassword.getUsername());
                request.setAttribute(DEFAULT_PARAMETER_PASSWORD, usernamePassword.getPassword());
            }
        }
        return super.resolveAuthenticationToken(request);
    }
}
