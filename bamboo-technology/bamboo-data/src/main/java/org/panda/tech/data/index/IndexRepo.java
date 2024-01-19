package org.panda.tech.data.index;

/**
 * 索引数据访问仓库
 *
 * @param <T> 实体类型
 */
public interface IndexRepo<T> {

    void save(T object);

    void delete(T object);

}
