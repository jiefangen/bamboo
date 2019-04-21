package org.panda.bamboo.product.client;

import org.panda.bamboo.product.client.vo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author jvfagan
 * @date:
 **/
public class OrderClient {

    @Autowired
    private RestTemplate restTemplate;

    public Order getOrder(String orderCode){
        ResponseEntity<Order> result = restTemplate.exchange("http://192.168.31.192:8081/v1/orders/{oderCode}",
                HttpMethod.GET,null,Order.class,orderCode);
        return result.getBody();
    }
}
