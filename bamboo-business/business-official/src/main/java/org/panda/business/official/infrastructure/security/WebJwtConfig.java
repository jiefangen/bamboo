package org.panda.business.official.infrastructure.security;

import org.panda.bamboo.common.util.lang.MathUtil;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.jwt.AbstractInternalJwtConfiguration;
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
    private static final String KEY;

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    /**
     * 静态代码块>构造代码块>构造函数>普通代码块
     * 1.静态代码块在类被加载的时候就运行了，而且只运行一次，并且优先于各种代码块以及构造函数。如果一个类中有多个静态代码块，会按照书写顺序依次执行。
     * 2.构造代码块在创建对象时被调用，每次创建对象都会调用一次，但是优先于构造函数执行。
     * 3.构造函数的功能主要用于在类的对象创建时定义初始化的状态。
     * 4.普通代码块的执行顺序和书写顺序一致
     */
    static { // 服务每次重启都会更新密钥key，提高系统安全性
        KEY = Long.toString(MathUtil.randomLong(10000000, 99999999));
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public String getSecretKey() {
        AesEncryptor aesEncryptor = new AesEncryptor();
        String secretKey = aesEncryptor.encrypt(appName, KEY);
        return secretKey;
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

}
