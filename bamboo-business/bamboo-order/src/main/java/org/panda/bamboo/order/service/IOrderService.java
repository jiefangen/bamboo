package org.panda.bamboo.order.service;

import org.panda.bamboo.order.vo.Order;

/**
 * @author jvfagan
 * @date:
 **/
public interface IOrderService {

    Order getOrderByCode(String code);
}
