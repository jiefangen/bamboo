package org.panda.tech.data.support;

import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.tech.data.Repox;
import org.panda.tech.data.model.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.Field;

/**
 * 数据访问仓库扩展支持
 *
 * @author fangen
 */
public abstract class RepoxSupport<T extends Entity> implements Repox<T> {

    @Autowired
    private RepoFactory repoFactory;
    @Autowired
    private DataAccessTemplateFactory accessTemplateFactory;

    /**
     * 获取实体类型<br>
     * 默认实现通过反射机制获取，子类可覆写直接返回具体实体的类型以优化性能
     *
     * @return 实体类型
     */
    protected Class<T> getEntityClass() {
        // 用指定类型的局部变量，以更好地类型转换，直接类型转换返回在IDEA中会编译失败
        Class<T> entityClass = ClassUtil.getActualGenericType(getClass(), 0);
        return entityClass;
    }

    @SuppressWarnings("unchecked")
    protected <R extends CrudRepository<T, K>, K> R getRepository() {
        if (this instanceof CrudRepository) {
            return (R) this;
        }
        return this.repoFactory.getRepository(getEntityClass());
    }

    protected DataAccessTemplate getAccessTemplate() {
        return this.accessTemplateFactory.getDataAccessTemplate(getEntityClass());
    }

    protected Class<?> getPropertyClass(String propertyName) {
        Field field = ClassUtil.findField(getEntityClass(), propertyName);
        return field == null ? null : field.getType();
    }

}
