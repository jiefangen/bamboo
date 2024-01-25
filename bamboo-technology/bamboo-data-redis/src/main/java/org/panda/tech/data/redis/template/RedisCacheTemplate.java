package org.panda.tech.data.redis.template;

import org.panda.tech.data.redis.RedisConstants;

/**
 * Redis缓存访问模版
 */
public class RedisCacheTemplate extends CacheTemplate<Object> {
    @Override
    protected String getRedisBeanName() {
        return RedisConstants.DEFAULT_BEAN_NAME;
    }
}
