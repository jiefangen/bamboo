package org.panda.bamboo.order.service;

import org.panda.bamboo.order.vo.Order;
import org.springframework.stereotype.Service;

/**
 * @author jvfagan
 * @date:
 **/
@Service
public class OrderService implements IOrderService {

    @Override
    public Order getOrderByCode(String code) {
        Order order = new Order();
        order.setCode(code);
        return order;
    }
}
