package org.panda.tech.core.jwt;

/**
 * 简单内部JWT配置
 */
public class SimpleInternalJwtConfiguration implements InternalJwtConfiguration {

    private String appName;
    private String secretKey;
    private int expiredIntervalSeconds;

    public SimpleInternalJwtConfiguration(String appName, String secretKey, int expiredIntervalSeconds) {
        this.appName = appName;
        this.secretKey = secretKey;
        this.expiredIntervalSeconds = expiredIntervalSeconds;
    }

    @Override
    public String getAppName() {
        return this.appName;
    }

    @Override
    public String getSecretKey() {
        return this.secretKey;
    }

    @Override
    public int getExpiredIntervalSeconds() {
        return this.expiredIntervalSeconds;
    }

}
