package org.panda.business.helper.app.infrastructure.security;

import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.jwt.internal.AbstractInternalJwtConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * AppJwt配置器
 *
 * @author fangen
 **/
@Configuration
public class AppJwtConfig extends AbstractInternalJwtConfiguration {
    /**
     * 固定默认的key，每次启动都会是相同的密钥
     */
    private static final String TOKEN_KEY = "d8MBgVBT3EE";
    /**
     * 失效时间
     */
    private static final int EXPIRED_INTERVAL = 86400;

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    static { // 服务每次重启都会更新密钥key，提高系统安全性
//        TOKEN_KEY = StringUtil.randomNormalMixeds(11);
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
        return EXPIRED_INTERVAL;
    }

}
