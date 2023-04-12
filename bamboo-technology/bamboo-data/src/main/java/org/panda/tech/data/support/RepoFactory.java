package org.panda.tech.data.support;

import org.panda.tech.data.Repox;
import org.panda.tech.data.model.entity.Entity;
import org.springframework.data.repository.CrudRepository;

/**
 * 数据访问仓库工厂
 *
 * @author fangen
 */
public interface RepoFactory {

    <R extends CrudRepository<T, K>, T extends Entity, K> R getRepository(Class<T> entityClass);

    <R extends Repox<T>, T extends Entity> R getRepox(Class<T> entityClass);

}
