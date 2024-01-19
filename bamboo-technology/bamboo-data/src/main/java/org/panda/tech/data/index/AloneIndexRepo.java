package org.panda.tech.data.index;

/**
 * 独立索引数据访问仓库。独立索引对象没有所属者，该类型的所有索引对象存储于同一个存储目录中。
 *
 * @param <T> 独立索引对象类型
 */
public interface AloneIndexRepo<T> extends IndexRepo<T> {

    long getSpaceSize();

    boolean isSearchable();

    void commit();

    void rollback();

    void clear();

}
