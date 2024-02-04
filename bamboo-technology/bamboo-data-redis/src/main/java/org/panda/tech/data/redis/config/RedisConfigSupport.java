package org.panda.tech.data.redis.config;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.data.redis.RedisConstants;
import org.panda.tech.data.redis.lock.RedisDistributedLock;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * 扩展Redis分布式锁服务配置支持
 *
 * @author fangen
 **/
public abstract class RedisConfigSupport extends RedisTemplateConfigSupport {

    protected String getRegistryKey() {
        return null;
    }

    // 默认锁定时间60s，重载该方法自定义锁定时间
    protected long getExpireAfter() {
        return RedisConstants.DEFAULT_EXPIRE_UNUSED;
    }

    @Bean(destroyMethod = "destroy")
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        String registryKey = getRegistryKey();
        if (StringUtils.isEmpty(getRegistryKey())) {
            registryKey = "lock";
        } else {
            registryKey += Strings.MINUS + "lock";
        }
        return new RedisLockRegistry(redisConnectionFactory, registryKey, getExpireAfter());
    }

    @Bean
    public RedisDistributedLock redisDistributedLock() {
        return new RedisDistributedLock();
    }
}
