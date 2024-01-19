package org.panda.tech.data.model.entity.unity;

import java.io.Serializable;

/**
 * 从属索引单体
 *
 * @param <K> 标识类型
 * @param <O> 所属者类型
 */
public interface OwnedIndexUnity<K extends Serializable, O extends Serializable>
        extends IndexUnity<K>, OwnedUnity<K, O> {
}
