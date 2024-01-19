package org.panda.tech.data.cache;

import java.util.List;

/**
 * 缓存数据访问仓库
 *
 * @param <T> 实体类型
 * @param <K> 标识类型
 */
public interface CacheRepo<T, K> {

    void save(T object);

    void delete(T object);

    T deleteByKey(K key);

    void deleteAll();

    List<T> findAll();

    T find(K key);

    boolean exists(K key);

}
