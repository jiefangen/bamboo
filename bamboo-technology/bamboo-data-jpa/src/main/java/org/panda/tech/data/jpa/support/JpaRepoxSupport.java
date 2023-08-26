package org.panda.tech.data.jpa.support;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.panda.tech.data.jpa.util.OqlUtil;
import org.panda.tech.data.model.entity.Entity;
import org.panda.tech.data.model.query.FieldOrder;
import org.panda.tech.data.model.query.Paged;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.model.query.spec.Paging;
import org.panda.tech.data.model.query.spec.QueryIgnoring;
import org.panda.tech.data.support.RepoxSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * JPA的数据访问仓库扩展支持
 */
public abstract class JpaRepoxSupport<T extends Entity> extends RepoxSupport<T> implements JpaRepox<T> {

    @Override
    public JpaAccessTemplate getAccessTemplate() {
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

    protected <E> QueryResult<E> query(CharSequence recordQl, CharSequence totalQl, Map<String, Object> params,
                                       int pageSize, int pageNo, List<FieldOrder> orders) {
        Long total = null;
        if (pageSize > 0 && totalQl != null) {
            total = getAccessTemplate().count(totalQl, params);
        }

        List<E> records;
        // 已知总数为0或无需查询记录清单，则不查询记录清单
        if ((total != null && total == 0) || recordQl == null) {
            records = new ArrayList<>();
        } else {
            String orderString = OqlUtil.buildOrderString(orders);
            if (StringUtils.isNotBlank(orderString)) {
                if (recordQl instanceof StringBuilder) {
                    ((StringBuilder) recordQl).append(orderString);
                } else if (recordQl instanceof StringBuffer) {
                    ((StringBuffer) recordQl).append(orderString);
                } else {
                    recordQl = recordQl + orderString;
                }
            }

            // 分页查询但未指定取数语句，未取总数，则多查询一条记录，以判断是否还有更多数据
            if (pageSize > 0 && totalQl == null) {
                records = getAccessTemplate().listWithOneMore(recordQl, params, pageSize, pageNo);
                boolean morePage = records.size() > pageSize;
                if (morePage) {
                    records.remove(records.size() - 1);
                }
                return new QueryResult<>(records, new Paged(pageSize, pageNo, morePage));
            }

            records = getAccessTemplate().list(recordQl, params, pageSize, pageNo);
            if (pageSize <= 0) { // 非分页查询，总数为结果记录条数
                total = (long) records.size();
            }
        }
        return QueryResult.of(records, pageSize, pageNo, total, orders);
    }

    protected <E> QueryResult<E> query(CharSequence ql, Map<String, Object> params, int pageSize, int pageNo,
                                       List<FieldOrder> orders, QueryIgnoring ignoring) {
        String totalQl = null;
        if (pageSize > 0 && ignoring != QueryIgnoring.TOTAL) {
            totalQl = ql.toString();
            totalQl = "select count(*) " + totalQl.substring(totalQl.indexOf("from "));
        }
        if (ignoring == QueryIgnoring.RECORD) {
            ql = null;
        }
        return query(ql, totalQl, params, pageSize, pageNo, orders);
    }

    public <E> QueryResult<E> query(CharSequence ql, Map<String, Object> params, Paging paging) {
        if (paging == null) {
            return query(ql, params, 0, 1, null, null);
        }
        return query(ql, params, paging.getPageSize(), paging.getPageNo(), paging.getOrders(), paging.getIgnoring());
    }

    public <E> QueryResult<E> query(CharSequence ql, Map<String, Object> params, int pageSize, int pageNo,
                                       FieldOrder... orders) {
        return query(ql, params, pageSize, pageNo, Arrays.asList(orders), null);
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
