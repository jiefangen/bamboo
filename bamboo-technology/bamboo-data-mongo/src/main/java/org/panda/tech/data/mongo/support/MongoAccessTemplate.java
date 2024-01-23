package org.panda.tech.data.mongo.support;

import org.panda.bamboo.common.util.lang.CollectionUtil;
import org.panda.tech.data.common.RepoUtil;
import org.panda.tech.data.model.entity.Entity;
import org.panda.tech.data.model.query.FieldOrder;
import org.panda.tech.data.model.query.Pagination;
import org.panda.tech.data.support.DataAccessTemplate;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MongoDB的数据访问模板
 */
public class MongoAccessTemplate implements DataAccessTemplate {

    private String schema = RepoUtil.DEFAULT_SCHEMA_NAME;
    private final MongoOperations mongoOperations;

    public MongoAccessTemplate(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public MongoAccessTemplate(String schema, MongoOperations mongoOperations) {
        this.schema = schema;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public String getSchema() {
        return this.schema;
    }

    @Override
    public Iterable<Class<?>> getEntityClasses() {
        List<Class<?>> entityClasses = new ArrayList<>();
        this.mongoOperations.getConverter().getMappingContext().getPersistentEntities().forEach(entity -> {
            entityClasses.add(entity.getType());
        });
        return entityClasses;
    }

    public MongoOperations getMongoOperations() {
        return this.mongoOperations;
    }

    public void applyPagingToQuery(Query query, int pageSize, int pageNo, boolean oneMore) {
        if (pageSize > 0) { // 用页大小判断是否分页查询
            if (pageNo <= 0) { // 页码最小为1
                pageNo = 1;
            }
            query.skip((long) pageSize * (pageNo - 1));
            query.limit(oneMore ? (pageSize + 1) : pageSize);
        }
    }

    public <T extends Entity> List<T> list(Class<T> entityClass, Query query) {
        return this.mongoOperations.find(query, entityClass);
    }

    public <T extends Entity> List<T> list(Class<T> entityClass, Query query, int pageSize, int pageNo) {
        applyPagingToQuery(query, pageSize, pageNo, false);
        return list(entityClass, query);
    }

    public <T extends Entity> List<T> list(Class<T> entityClass, Query query, int pageSize, int pageNo,
            List<FieldOrder> orders) {
        applyPagingToQuery(query, pageSize, pageNo, false);
        query.with(RepoUtil.toSort(orders));
        return list(entityClass, query);
    }

    public <T extends Entity> List<T> list(Class<T> entityClass, Query query, Pagination pagination) {
        query.with(RepoUtil.toPageable(pagination));
        return list(entityClass, query);
    }

    public <T extends Entity> T first(Class<T> entityClass, Query query) {
        List<T> list = list(entityClass, query, 1, 1);
        return CollectionUtil.getFirst(list, null);
    }

    public long count(Class<?> entityClass, Query query) {
        return this.mongoOperations.count(query, entityClass);
    }

    public <T extends Entity> List<T> listWithOneMore(Class<T> entityClass, Query query, int pageSize, int pageNo) {
        applyPagingToQuery(query, pageSize, pageNo, true);
        return list(entityClass, query);
    }

    public <T extends Entity> List<T> listWithOneMore(Class<T> entityClass, Query query, int pageSize, int pageNo,
            FieldOrder... orders) {
        query.with(RepoUtil.toSort(orders));
        return listWithOneMore(entityClass, query, pageSize, pageNo);
    }

    public <T extends Entity> List<T> listWithOneMore(Class<T> entityClass, Query query, Pagination pagination) {
        query.with(RepoUtil.toSort(pagination.getOrders()));
        return listWithOneMore(entityClass, query, pagination.getPageSize(), pagination.getPageNo());
    }

    public long update(Class<?> entityClass, Query query, String propertyName, Object propertyValue) {
        Update update = Update.update(propertyName, propertyValue);
        return this.mongoOperations.upsert(query, update, entityClass).getModifiedCount();
    }

    public long update(Class<?> entityClass, Query query, Map<String, Object> values) {
        Update update = new Update();
        values.forEach(update::set);
        return this.mongoOperations.upsert(query, update, entityClass).getModifiedCount();
    }

    public long delete(Class<?> entityClass, Query query) {
        return this.mongoOperations.remove(query, entityClass).getDeletedCount();
    }

}
