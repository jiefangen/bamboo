package org.panda.business.official.infrastructure.cache;

import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.data.redis.config.RedisConfigSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisCacheConfig extends RedisConfigSupport {

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    @Override
    protected String getRegistryKey() {
        return appName;
    }

    @Override
    protected RedisSerializer<Object> getValueSerializer() {
        return super.getValueSerializer();
    }
}
