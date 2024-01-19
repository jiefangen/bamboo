package org.panda.tech.data.model.entity.relation;

import java.io.Serializable;
import java.util.Objects;

/**
 * 抽象的关系
 *
 * @param <L> 左标识类型
 * @param <R> 右标识类型
 */
public abstract class AbstractRelation<L extends Serializable, R extends Serializable> implements Relation<L, R> {

    public abstract <K extends RelationKey<L, R>> K getId();

    @Override
    public L getLeftId() {
        RelationKey<L, R> id = getId();
        return id == null ? null : id.getLeft();
    }

    @Override
    public R getRightId() {
        RelationKey<L, R> id = getId();
        return id == null ? null : id.getRight();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeftId(), getRightId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Relation<L, R> other = (Relation<L, R>) obj;
        return Objects.equals(getLeftId(), other.getLeftId())
                && Objects.equals(getRightId(), other.getRightId());
    }

}
