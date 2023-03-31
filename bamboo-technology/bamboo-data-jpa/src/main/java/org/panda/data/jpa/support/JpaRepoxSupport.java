package org.panda.data.jpa.support;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.panda.data.jpa.JpaRepox;
import org.panda.data.model.entity.Entity;
import org.panda.data.support.RepoxSupport;

/**
 * JPA的数据访问仓库扩展支持
 *
 * @author fangen
 */
public abstract class JpaRepoxSupport<T extends Entity> extends RepoxSupport<T> implements JpaRepox<T> {

    @Override
    protected JpaAccessTemplate getAccessTemplate() {
        return (JpaAccessTemplate) super.getAccessTemplate();
    }

    @Override
    public String getEntityName() {
        return getEntityClass().getName();
    }

    @Override
    public void flush() {
        getAccessTemplate().flush();
    }

    @Override
    public void refresh(T entity) {
        getAccessTemplate().refresh(entity);
    }

    protected final String getTableName() {
        return getAccessTemplate().getTableName(getEntityName());
    }

    private PersistentClass getPersistentClass() {
        return getAccessTemplate().getPersistentClass(getEntityName());
    }

    protected final Column getColumn(String propertyName) {
        Property property = getPersistentClass().getProperty(propertyName);
        return (Column) property.getColumnIterator().next();
    }

    protected final String getColumnName(String propertyName) {
        return getColumn(propertyName).getName();
    }

}
