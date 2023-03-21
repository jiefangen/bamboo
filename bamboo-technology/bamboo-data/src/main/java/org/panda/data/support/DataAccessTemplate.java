package org.panda.data.support;

/**
 * 数据访问模板定义
 *
 * @author fangen
 */
public interface DataAccessTemplate {

    /**
     * @return 数据源模式名称
     */
    String getSchema();

    /**
     * @return 管理的所有实体类型集合
     */
    Iterable<Class<?>> getEntityClasses();

}
