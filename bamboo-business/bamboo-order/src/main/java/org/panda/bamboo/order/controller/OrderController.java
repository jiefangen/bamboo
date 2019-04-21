package org.panda.bamboo.order.controller;

import org.panda.bamboo.order.service.IOrderService;
import org.panda.bamboo.order.vo.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *
 *
 * @author jvfagan
 * @date: 2019/04/15
 **/
@RestController
@RequestMapping(value = "v1/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService orderService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/{oderCode}",method = RequestMethod.GET)
    public Order getOrder(@PathVariable String oderCode){
        logger.info("Get order by code: {} from port: {}" , oderCode, request.getServerPort());
        Order order = orderService.getOrderByCode(oderCode);
        return order;
    }
}
