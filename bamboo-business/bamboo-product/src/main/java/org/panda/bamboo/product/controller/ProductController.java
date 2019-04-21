package org.panda.bamboo.product.controller;

import org.panda.bamboo.product.client.OrderClient;
import org.panda.bamboo.product.client.vo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author jvfagan
 * @date: 2019/04/15
 **/
@RestController
@RequestMapping(value = "v1/products")
public class ProductController {

    @Autowired
    OrderClient orderClient;

    @RequestMapping(value = "/{productCode}",method = RequestMethod.GET)
    public Order getOrder(@PathVariable String productCode){
        Order order =orderClient.getOrder(productCode);
        return order;
    }
}
