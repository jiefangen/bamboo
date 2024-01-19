package org.panda.tech.data.model.entity.relation;

import org.panda.bamboo.common.model.tuple.Binate;

import java.io.Serializable;

/**
 * 关系的主键类型
 */
public interface RelationKey<L extends Serializable, R extends Serializable> extends Binate<L, R>, Serializable {

}
