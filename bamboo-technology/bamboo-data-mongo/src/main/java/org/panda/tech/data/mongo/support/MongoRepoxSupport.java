package org.panda.tech.data.mongo.support;

import org.panda.tech.data.model.entity.Entity;
import org.panda.tech.data.model.query.FieldOrder;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.model.query.spec.Paging;
import org.panda.tech.data.model.query.spec.QueryIgnoring;
import org.panda.tech.data.mongo.util.MongoQueryUtil;
import org.panda.tech.data.support.RepoxSupport;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MongoDB数据访问仓库扩展支持
 */
public abstract class MongoRepoxSupport<T extends Entity> extends RepoxSupport<T> {

    @Override
    public String getEntityName() {
        return getAccessTemplate().getMongoOperations().getCollectionName(getEntityClass());
    }

    @Override
    protected MongoAccessTemplate getAccessTemplate() {
        return (MongoAccessTemplate) super.getAccessTemplate();
    }

    private QueryResult<T> query(List<Criteria> criteriaList, QueryIgnoring ignoring, int pageSize, int pageNo,
                                 List<FieldOrder> orders) {
        Query query = MongoQueryUtil.buildQuery(criteriaList);
        Long total = null;
        if (pageSize > 0 && ignoring != QueryIgnoring.TOTAL) { // 需分页查询且不忽略总数时，才获取总数
            total = getAccessTemplate().count(getEntityClass(), query);
        }
        List<T> records;
        if ((total != null && total == 0) || ignoring == QueryIgnoring.RECORD) {
            records = new ArrayList<>();
        } else {
            records = getAccessTemplate().list(getEntityClass(), query, pageSize, pageNo, orders);
        }
        return QueryResult.of(records, pageSize, pageNo, total, orders);
    }

    protected QueryResult<T> query(List<Criteria> criteriaList, Paging paging) {
        return query(criteriaList, paging.getIgnoring(), paging.getPageSize(), paging.getPageNo(),
                paging.getOrders());
    }

    protected QueryResult<T> query(List<Criteria> criteriaList, int pageSize, int pageNo, FieldOrder... orders) {
        return query(criteriaList, null, pageSize, pageNo, Arrays.asList(orders));
    }

}
