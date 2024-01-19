package org.panda.tech.data.cache;

import org.panda.tech.data.model.entity.unity.Unity;

import java.io.Serializable;

public interface UnityCacheRepo<T extends Unity<K>, K extends Serializable> extends CacheRepo<T, K> {

    void deleteById(K id);

}
