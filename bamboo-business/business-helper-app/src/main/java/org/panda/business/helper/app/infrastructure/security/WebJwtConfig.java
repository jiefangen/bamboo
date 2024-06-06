package org.panda.business.helper.app.infrastructure.security;

import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.jwt.internal.AbstractInternalJwtConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * WebJwt配置器
 *
 * @author fangen
 **/
@Configuration
public class WebJwtConfig extends AbstractInternalJwtConfiguration {
    /**
     * 随机生成的key，每次启动都会有不同的密钥产生
     */
    private static final String TOKEN_KEY;

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    static { // 服务每次重启都会更新密钥key，提高系统安全性
        TOKEN_KEY = StringUtil.randomNormalMixeds(11);
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public String getSecretKey() {
        AesEncryptor aesEncryptor = new AesEncryptor();
        return aesEncryptor.encrypt(appName, TOKEN_KEY);
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @Override
    public int getExpiredIntervalSeconds() {
        return super.getExpiredIntervalSeconds();
    }

}
