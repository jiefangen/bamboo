package org.panda.tech.data.redis.support;

import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.tech.data.cache.CacheRepo;

/**
 * Redis缓存数据支持
 *
 * @param <V> 值类型
 */
public abstract class RedisCacheRepoSupport<V, K> implements CacheRepo<V, K> {

    protected Class<V> getValueClass() {
        return ClassUtil.getActualGenericType(getClass(), 0);
    }

    protected Class<K> getKeyClass() {
        return ClassUtil.getActualGenericType(getClass(), 1);
    }

    /**
     * 获取缓存关键字前缀，前缀用于在redis中区分实体类型，默认为实体类全名
     *
     * @return 缓存关键字前缀
     */
    protected String getKeyPrefix() {
        return getValueClass().getName();
    }

}
