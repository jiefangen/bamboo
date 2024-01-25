package org.panda.tech.data.redis.support;

import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.List;

/**
 * ForValue方式的Redis缓存数据支持
 *
 * @param <V> 实体类型
 */
public abstract class RedisValueCacheRepoSupport<V, K> extends RedisCacheRepoSupport<V, K> implements InitializingBean {

    @Autowired
    protected RedisConnectionFactory connectionFactory;
    private RedisValueTemplate<K, V> accessTemplate;

    @Override
    public void afterPropertiesSet() {
        this.accessTemplate = new RedisValueTemplate<>(this.connectionFactory, getKeyClass(), getValueClass(),
                this::getKeyPrefix);
    }

    protected final RedisValueTemplate<K, V> getAccessTemplate() {
        return this.accessTemplate;
    }

    @Override
    protected String getKeyPrefix() {
        return getValueClass().getName() + Strings.COLON;
    }

    /**
     * 获取指定值对象的缓存关键字值
     *
     * @param object 值对象
     * @return 指定值对象的缓存关键字值
     */
    protected abstract K getKeyValue(V object);

    @Override
    public void save(V object) {
        K keyValue = getKeyValue(object);
        getAccessTemplate().set(keyValue, object);
    }

    @Override
    public void delete(V object) {
        K keyValue = getKeyValue(object);
        deleteByKey(keyValue);
    }

    @Override
    public V deleteByKey(K key) {
        return getAccessTemplate().delete(key);
    }

    @Override
    public void deleteAll() {
        getAccessTemplate().deleteAll();
    }

    @Override
    public List<V> findAll() {
        return getAccessTemplate().list(value -> true);
    }

    @Override
    public V find(K key) {
        return getAccessTemplate().get(key);
    }

    @Override
    public boolean exists(K key) {
        return getAccessTemplate().hasKey(key);
    }

}
