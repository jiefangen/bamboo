package org.panda.data.model.query;

import java.util.LinkedList;
import java.util.List;

/**
 * 分页查询条件。通过创建子类附带更多的查询条件
 *
 * @author fangen
 */
public class QueryParam extends Pagination implements QueryModel {
    private static final long serialVersionUID = -7685463756368907614L;

    private List<FieldOrder> orders;

    public QueryParam() {
    }

    public QueryParam(int pageSize, int pageNo) {
        super(pageSize, pageNo);
    }

    public QueryParam(int pageSize, int pageNo, List<FieldOrder> orders) {
        super(pageSize, pageNo);
        this.orders = orders;
    }

    public List<FieldOrder> getOrders() {
        return this.orders;
    }

    public void setOrders(List<FieldOrder> orders) {
        this.orders = orders;
    }

    public void addOrder(FieldOrder order) {
        if (order != null) {
            if (this.orders == null) {
                this.orders = new LinkedList<>();
            }
            this.orders.removeIf(o -> o.getName().equals(order.getName()));
            this.orders.add(order);
        }
    }

    public void addOrder(String fieldName, boolean desc) {
        addOrder(new FieldOrder(fieldName, desc));
    }

}
