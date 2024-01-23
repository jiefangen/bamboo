package org.panda.tech.data.mongo.support;

import org.panda.tech.data.UnityRepox;
import org.panda.tech.data.model.entity.unity.Unity;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * MongoDB单体数据访问仓库扩展支持
 */
public abstract class MongoUnityRepoxSupport<T extends Unity<K>, K extends Serializable> extends MongoRepoxSupport<T>
        implements UnityRepox<T, K> {

    protected final T find(K id) {
        CrudRepository<T, K> repository = getRepository();
        return repository.findById(id).orElse(null);
    }

    @Override
    public <N extends Number> T increaseNumber(K id, String propertyName, N step, N limit) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
