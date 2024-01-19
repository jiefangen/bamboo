package org.panda.tech.data.model.entity.unity;

import org.panda.bamboo.common.model.spec.Owned;

import java.io.Serializable;

/**
 * 从属单体
 *
 * @param <K> 标识类型
 * @param <O> 所属者类型
 */
public interface OwnedUnity<K extends Serializable, O extends Serializable> extends Unity<K>, Owned<O> {
}
