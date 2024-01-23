package org.panda.tech.data.mongo.support;

import org.panda.tech.data.OwnedUnityRepox;
import org.panda.tech.data.model.entity.unity.OwnedUnity;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;

/**
 * MongoDB从属单体的数据访问扩展仓库
 * 复杂关联关系等特殊场景使用，一般场景使用{@link MongoUnityRepoxSupport}即可满足
 */
public abstract class MongoOwnedUnityRepoxSupport<T extends OwnedUnity<K, O>, K extends Serializable, O extends Serializable>
        extends MongoUnityRepoxSupport<T, K> implements OwnedUnityRepox<T, K, O> {

    /**
     * @return 所属者属性名
     */
    protected abstract String getOwnerProperty();

    @Override
    public long countByOwner(O owner) {
        String ownerProperty = getOwnerProperty();
        if (ownerProperty == null) {
            throw new UnsupportedOperationException();
        }
        Query query = new Query(Criteria.where(ownerProperty).is(owner));
        return getAccessTemplate().count(getEntityClass(), query);
    }

    @Override
    public T findByOwnerAndId(O owner, K id) {
        if (id == null) {
            return null;
        }
        String ownerProperty = getOwnerProperty();
        if (ownerProperty == null) {
            T entity = find(id);
            if (entity != null && owner.equals(entity.getOwner())) {
                return entity;
            }
            return null;
        }
        Query query = new Query(Criteria.where(ownerProperty).is(owner).and("id").is(id));
        return getAccessTemplate().first(getEntityClass(), query);
    }

    @Override
    public <N extends Number> T increaseNumber(O owner, K id, String propertyName, N step, N limit) {
        // TODO
        throw new UnsupportedOperationException();
    }

}
