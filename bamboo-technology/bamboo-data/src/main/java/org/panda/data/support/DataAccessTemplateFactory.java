package org.panda.data.support;

/**
 * 数据访问模板定义
 *
 * @author fangen
 */
public interface DataAccessTemplateFactory {

    DataAccessTemplate getDataAccessTemplate(Class<?> entityClass);

}
