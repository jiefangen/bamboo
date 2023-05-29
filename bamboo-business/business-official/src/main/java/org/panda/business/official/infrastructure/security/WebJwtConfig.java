package org.panda.business.official.infrastructure.security;

import org.panda.bamboo.common.util.lang.MathUtil;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.spec.jwt.AbstractInternalJwtConfiguration;
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
    private static final String key = Long.toString(MathUtil.randomLong(10000000, 99999999));

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public String getSecretKey() {
        AesEncryptor aesEncryptor = new AesEncryptor();
        String secretKey = aesEncryptor.encrypt(appName, key);
        return secretKey;
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

}
