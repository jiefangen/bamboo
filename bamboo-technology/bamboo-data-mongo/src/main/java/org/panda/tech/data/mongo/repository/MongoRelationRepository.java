package org.panda.tech.data.mongo.repository;

import org.panda.tech.data.RelationRepox;
import org.panda.tech.data.model.entity.relation.Relation;
import org.panda.tech.data.model.entity.relation.RelationKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

/**
 * 为JPA关系访问库提供的便捷接口，非必须
 * 业务Repo接口可同时继承{@link MongoRepository}和{@link RelationRepox}达到相同的效果
 */
@NoRepositoryBean
public interface MongoRelationRepository<T extends Relation<L, R>, L extends Serializable, R extends Serializable>
        extends MongoRepository<T, RelationKey<L, R>>, RelationRepox<T, L, R> {

    @Override
    default Optional<T> findById(RelationKey<L, R> id) {
        return Optional.ofNullable(find(id.getLeft(), id.getRight()));
    }

}
