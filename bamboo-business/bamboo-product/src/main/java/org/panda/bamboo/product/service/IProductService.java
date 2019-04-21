package org.panda.bamboo.product.service;

import org.panda.bamboo.product.client.vo.Order;

/**
 * @author jvfagan
 * @date:
 **/
public interface IProductService {

    Order getOrderByCode(String code);
}
