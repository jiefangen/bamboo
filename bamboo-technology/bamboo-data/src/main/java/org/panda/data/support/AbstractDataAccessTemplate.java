package org.panda.data.support;

/**
 * 抽象数据访问模板
 *
 * @author fangen
 */
public abstract class AbstractDataAccessTemplate {

    abstract DataAccessTemplate getDataAccessTemplate(Class<?> entityClass);

}
