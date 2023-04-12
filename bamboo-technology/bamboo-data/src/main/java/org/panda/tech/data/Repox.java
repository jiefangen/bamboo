package org.panda.tech.data;

import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.tech.data.model.entity.Entity;
import org.springframework.data.repository.Repository;


/**
 * 数据访问仓库扩展
 * 用于定义在spring-data的{@link Repository}标准规范方法之外的扩展的数据访问方法
 * 一个实体类型必须要有对应的{@link Repository}，但可以没有对应的{@link Repox}
 *
 * @param <T> 实体类型
 * @author fangen
 */
public interface Repox<T extends Entity> {

    default String getEntityName() {
        return ClassUtil.getActualGenericType(getClass(), Repox.class, 0).getName();
    }

}
