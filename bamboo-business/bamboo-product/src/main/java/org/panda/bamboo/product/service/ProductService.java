package org.panda.bamboo.product.service;

import org.panda.bamboo.product.client.vo.Order;
import org.springframework.stereotype.Service;

/**
 * @author jvfagan
 * @date:
 **/
@Service
public class ProductService implements IProductService {

//    @Autowired
//    private OrderClient orderClient;

    @Override
    public Order getOrderByCode(String code) {
        Order order = new Order();
        order.setCode(code);
        return order;
    }
}
