package org.panda.tech.data.jpa.support;

import org.panda.tech.data.Repox;
import org.panda.tech.data.model.entity.Entity;

/**
 * JPA的数据访问仓库扩展
 *
 * @author fangen
 */
public interface JpaRepox<T extends Entity> extends Repox<T> {

    void flush();

    void refresh(T entity);

}
