package org.panda.tech.data.model.query.spec;

import org.panda.tech.data.model.query.FieldOrder;

import java.util.List;

/**
 * 分页的
 */
public interface Paging {

    int getPageSize();

    int getPageNo();

    List<FieldOrder> getOrders();

    QueryIgnoring getIgnoring();

}
