package org.panda.tech.data.index;

import org.panda.bamboo.common.model.spec.Owned;

import java.io.Serializable;

/**
 * 从属索引数据访问仓库。从属索引对象具有所属者，该类型的索引对象根据所属者不同而存储于不同的存储目录中。
 *
 * @param <T> 从属索引对象类型
 * @param <O> 所属者类型
 */
public interface OwnedIndexRepo<T extends Owned<O>, O extends Serializable> extends IndexRepo<T> {

    long getSpaceSize(O owner);

    boolean isSearchable(O owner);

    void commit(O owner);

    void rollback(O owner);

    void clear(O owner);

}
