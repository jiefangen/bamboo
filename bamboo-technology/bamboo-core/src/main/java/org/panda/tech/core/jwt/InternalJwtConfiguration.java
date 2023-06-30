package org.panda.tech.core.jwt;

import org.apache.commons.lang3.StringUtils;

/**
 * 内部JWT配置
 */
public interface InternalJwtConfiguration {

    /**
     * @return 应用名称
     */
    String getAppName();

    /**
     * @return 密钥
     */
    String getSecretKey();

    /**
     * @return 过期间隔秒数，应大于服务的启动时间
     */
    int getExpiredIntervalSeconds();

    default boolean isValid() {
        return getExpiredIntervalSeconds() > 0 && StringUtils.isNotBlank(getSecretKey());
    }

}
