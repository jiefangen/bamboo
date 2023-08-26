package org.panda.tech.data.jpa.support;

import org.hibernate.boot.Metadata;
import org.hibernate.mapping.PersistentClass;
import org.panda.bamboo.common.util.lang.CollectionUtil;
import org.panda.tech.data.jpa.hibernate.MetadataProvider;
import org.panda.tech.data.model.entity.Entity;
import org.panda.tech.data.support.DataAccessTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.*;
import java.util.Map.Entry;

/**
 * JPA的数据访问模板
 *
 * @author fangen
 */
public class JpaAccessTemplate implements DataAccessTemplate {

    private String schema = "default";
    private EntityManagerFactory entityManagerFactory;
    private MetadataProvider metadataProvider;
    private boolean nativeMode;
    private int maxPageSize = 200;

    public JpaAccessTemplate(EntityManagerFactory entityManagerFactory, MetadataProvider metadataProvider) {
        Assert.notNull(entityManagerFactory, "entityManagerFactory must not be null");
        Assert.notNull(metadataProvider, "metadataProvider must not be null");
        this.entityManagerFactory = entityManagerFactory;
        this.metadataProvider = metadataProvider;
    }

    public JpaAccessTemplate(String schema, EntityManagerFactory entityManagerFactory,
            MetadataProvider metadataProvider) {
        this(entityManagerFactory, metadataProvider);
        Assert.notNull(schema, "schema must not be null");
        this.schema = schema;
    }

    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = Math.min(maxPageSize, 1000); // 最大允许每页1000条
    }

    @Override
    public String getSchema() {
        return this.schema;
    }

    @Override
    public Iterable<Class<?>> getEntityClasses() {
        List<Class<?>> entityClasses = new ArrayList<>();
        getEntityManagerFactory().getMetamodel().getManagedTypes().forEach(type -> {
            entityClasses.add(type.getJavaType());
        });
        return entityClasses;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        // 每次都重新获取，以免获取的实体管理器在事务会话已关闭后还继续使用，在同一个事务会话中有缓存，无需担心性能
        // 如果此处报NullPointerException，请检查方法调用链上是否有事务注解
        return EntityManagerFactoryUtils.getTransactionalEntityManager(getEntityManagerFactory());
    }

    public PersistentClass getPersistentClass(String entityName) {
        Metadata metadata = this.metadataProvider.getMetadata();
        return metadata == null ? null : metadata.getEntityBinding(entityName);
    }

    public String getTableName(String entityName) {
        PersistentClass persistentClass = getPersistentClass(entityName);
        if (persistentClass != null) {
            return persistentClass.getTable().getName();
        }
        return null;
    }

    /**
     * 创建对应的原生SQL方式的访问模板
     *
     * @return 原生SQL方式的访问模板
     */
    public JpaAccessTemplate createNative() {
        JpaAccessTemplate template = new JpaAccessTemplate(this.schema, getEntityManagerFactory(),
                this.metadataProvider);
        template.nativeMode = true;
        return template;
    }

    public void flush() {
        getEntityManager().flush();
    }

    public void refresh(Entity entity) {
        getEntityManager().refresh(entity);
    }

    // 非分页查询
    public <T> List<T> list(CharSequence ql, String paramName, Object paramValue) {
        return list(ql, paramName, paramValue, 0, 0);
    }

    // 非分页查询
    public <T> List<T> list(CharSequence ql, Map<String, ?> params) {
        return list(ql, params, 0, 0);
    }

    // 非分页查询
    public <T> List<T> list(CharSequence ql, List<?> params) {
        return list(ql, params, 0, 0);
    }

    // 非分页查询
    public <T> List<T> list(CharSequence ql) {
        return list(ql, (Map<String, ?>) null);
    }

    public <T> T first(CharSequence ql, String paramName, Object paramValue) {
        List<T> list = list(ql, paramName, paramValue, 1, 1);
        return CollectionUtil.getFirst(list, null);
    }

    public <T> T first(CharSequence ql, Map<String, ?> params) {
        List<T> list = list(ql, params, 1, 1);
        return CollectionUtil.getFirst(list, null);
    }

    public <T> T first(CharSequence ql, List<?> params) {
        List<T> list = list(ql, params, 1, 1);
        return CollectionUtil.getFirst(list, null);
    }

    public <T> T first(CharSequence ql) {
        return first(ql, (Map<String, ?>) null);
    }

    public boolean exists(CharSequence ql, String paramName, Object paramValue) {
        return first(ql, paramName, paramValue) != null;
    }

    public boolean exists(CharSequence ql, Map<String, ?> params) {
        return first(ql, params) != null;
    }

    public boolean exists(CharSequence ql, List<?> params) {
        return first(ql, params) != null;
    }

    public boolean exists(CharSequence ql) {
        return first(ql) != null;
    }

    public long count(CharSequence ql, String paramName, Object paramValue) {
        Number value = first(ql, paramName, paramValue);
        return value == null ? 0 : value.longValue();
    }

    public long count(CharSequence ql, Map<String, ?> params) {
        Number value = first(ql, params);
        return value == null ? 0 : value.longValue();
    }

    public long count(CharSequence ql, List<?> params) {
        Number value = first(ql, params);
        return value == null ? 0 : value.longValue();
    }

    public long count(CharSequence ql) {
        return count(ql, (Map<String, ?>) null);
    }

    private Query createQuery(CharSequence ql) {
        if (this.nativeMode) {
            return getEntityManager().createNativeQuery(ql.toString());
        } else {
            return getEntityManager().createQuery(ql.toString());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(CharSequence ql, String paramName, Object paramValue, int pageSize, int pageNo) {
        Query query = createQuery(ql);
        applyParamToQuery(query, paramName, paramValue);
        applyPagingToQuery(query, pageSize, pageNo, false);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(CharSequence ql, Map<String, ?> params, int pageSize, int pageNo) {
        Query query = createQuery(ql);
        applyParamsToQuery(query, params);
        applyPagingToQuery(query, pageSize, pageNo, false);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(CharSequence ql, List<?> params, int pageSize, int pageNo) {
        Query query = createQuery(ql);
        applyParamsToQuery(query, params);
        applyPagingToQuery(query, pageSize, pageNo, false);
        return query.getResultList();
    }

    // 分页查询
    public <T> List<T> list(CharSequence ql, int pageSize, int pageNo) {
        return list(ql, (Map<String, ?>) null, pageSize, pageNo);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> listWithOneMore(CharSequence ql, String paramName, Object paramValue, int pageSize, int pageNo) {
        Query query = createQuery(ql);
        applyParamToQuery(query, paramName, paramValue);
        applyPagingToQuery(query, pageSize, pageNo, true);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> listWithOneMore(CharSequence ql, Map<String, ?> params, int pageSize, int pageNo) {
        Query query = createQuery(ql);
        applyParamsToQuery(query, params);
        applyPagingToQuery(query, pageSize, pageNo, true);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> listWithOneMore(CharSequence ql, List<?> params, int pageSize, int pageNo) {
        Query query = createQuery(ql);
        applyParamsToQuery(query, params);
        applyPagingToQuery(query, pageSize, pageNo, true);
        return query.getResultList();
    }

    /**
     * 分页查询，比指定的页大小多查出一条记录来，用于判断是否还有更多的记录
     *
     * @param ql       查询语句
     * @param pageSize 页大小
     * @param pageNo   页码
     * @param <T>      记录类型
     * @return 查询结果
     */
    public <T> List<T> listWithOneMore(CharSequence ql, int pageSize, int pageNo) {
        return listWithOneMore(ql, (Map<String, ?>) null, pageSize, pageNo);
    }

    public int update(CharSequence ql, String paramName, Object paramValue) {
        Query query = createQuery(ql);
        applyParamToQuery(query, paramName, paramValue);
        return query.executeUpdate();
    }

    public int update(CharSequence ql, Map<String, ?> params) {
        Query query = createQuery(ql);
        applyParamsToQuery(query, params);
        return query.executeUpdate();
    }

    public int update(CharSequence ql, List<?> params) {
        Query query = createQuery(ql);
        applyParamsToQuery(query, params);
        return query.executeUpdate();
    }

    public int update(CharSequence ul) {
        return update(ul, (Map<String, ?>) null);
    }

    private void applyParamsToQuery(Query query, Map<String, ?> params) {
        if (params != null) {
            for (Entry<String, ?> entry : params.entrySet()) {
                applyParamToQuery(query, entry.getKey(), entry.getValue());
            }
        }
    }

    private void applyParamToQuery(Query query, String name, Object value) {
        if (value instanceof Calendar) {
            query.setParameter(name, (Calendar) value, TemporalType.TIMESTAMP);
        } else if (value instanceof Date) {
            query.setParameter(name, (Date) value, TemporalType.TIMESTAMP);
        } else {
            query.setParameter(name, value);
        }
    }

    private void applyParamsToQuery(Query query, List<?> params) {
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                applyParamToQuery(query, i + 1, params.get(i));
            }
        }
    }

    /**
     * 设置查询参数到查询对象中
     *
     * @param query    查询对象
     * @param position 参数位置，从1开始计数
     * @param value    参数值
     */
    private void applyParamToQuery(Query query, int position, Object value) {
        if (value instanceof Calendar) {
            query.setParameter(position, (Calendar) value, TemporalType.TIMESTAMP);
        } else if (value instanceof Date) {
            query.setParameter(position, (Date) value, TemporalType.TIMESTAMP);
        } else {
            query.setParameter(position, value);
        }
    }

    private void applyPagingToQuery(Query query, int pageSize, int pageNo, boolean oneMore) {
        if (pageSize > 0) { // 用页大小判断是否分页查询
            if (pageSize > this.maxPageSize) {
                pageSize = this.maxPageSize;
            }
            if (pageNo <= 0) { // 页码最小为1
                pageNo = 1;
            }
            query.setFirstResult(pageSize * (pageNo - 1));
            query.setMaxResults(oneMore ? (pageSize + 1) : pageSize);
        }
    }

}
