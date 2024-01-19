package org.panda.tech.data.common;

import org.apache.commons.lang3.ArrayUtils;
import org.panda.tech.data.model.query.FieldOrder;
import org.panda.tech.data.model.query.Pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Repo工具类
 */
public class RepoUtil {

    private RepoUtil() {
    }

    /**
     * 默认模式名
     */
    public static final String DEFAULT_SCHEMA_NAME = "default";

    public static Order toOrder(FieldOrder fieldOrder) {
        if (fieldOrder == null) {
            return null;
        }
        if (fieldOrder.isDesc()) {
            return Order.desc(fieldOrder.getName());
        } else {
            return Order.asc(fieldOrder.getName());
        }
    }

    public static Sort toSort(FieldOrder... fieldOrders) {
        if (ArrayUtils.isEmpty(fieldOrders)) {
            return Sort.unsorted();
        }
        List<Order> orders = new ArrayList<>();
        for (FieldOrder fieldOrder : fieldOrders) {
            orders.add(toOrder(fieldOrder));
        }
        return Sort.by(orders);
    }

    public static Sort toSort(Collection<FieldOrder> fieldOrders) {
        if (CollectionUtils.isEmpty(fieldOrders)) {
            return Sort.unsorted();
        }
        List<Order> orders = new ArrayList<>();
        for (FieldOrder fieldOrder : fieldOrders) {
            orders.add(toOrder(fieldOrder));
        }
        return Sort.by(orders);
    }

    public static Pageable toPageable(Pagination pagination) {
        if (pagination == null || pagination.getPageSize() <= 0) {
            return Pageable.unpaged();
        }
        Sort sort = toSort(pagination.getOrders());
        int pageNo = pagination.getPageNo() - 1; // Pageable的页码从0开始计数
        if (pageNo < 0) {
            pageNo = 0;
        }
        return PageRequest.of(pageNo, pagination.getPageSize(), sort);
    }

    public static FieldOrder toFieldOrder(Order order) {
        if (order == null) {
            return null;
        }
        return new FieldOrder(order.getProperty(), order.isDescending());
    }

    public static List<FieldOrder> toFieldOrders(Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return null;
        }
        List<FieldOrder> fieldOrders = new ArrayList<>();
        sort.forEach(order -> {
            fieldOrders.add(toFieldOrder(order));
        });
        return fieldOrders;
    }

    public static Pagination toPagination(Pageable pageable) {
        List<FieldOrder> orders = toFieldOrders(pageable.getSort());
        int pageNo = pageable.getPageNumber() + 1; // Paging的页码从1开始计数
        if (pageNo < 1) {
            pageNo = 1;
        }
        Pagination pagination = new Pagination(pageable.getPageSize(), pageNo);
        pagination.setOrders(orders);
        return pagination;
    }

}
