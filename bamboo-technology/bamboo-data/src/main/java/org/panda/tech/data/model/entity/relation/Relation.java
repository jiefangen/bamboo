package org.panda.tech.data.model.entity.relation;

import org.panda.tech.data.model.entity.Entity;

import java.io.Serializable;

/**
 * 关系模型
 *
 * @param <L> 左标识类型
 * @param <R> 右标识类型
 */
public interface Relation<L extends Serializable, R extends Serializable> extends Entity {

    /**
     *
     * @return 左标识
     */
    L getLeftId();

    /**
     *
     * @return 右标识
     */
    R getRightId();

    @Override
    int hashCode();

    @Override
    boolean equals(Object obj);
}
