package org.panda.bamboo.order;

import org.panda.bamboo.order.vo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author jvfagan
 * @date:
 **/
public class EndpointTest {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    public Order getOrder(String orderCode){
        List<ServiceInstance> instances = discoveryClient.getInstances("orderservice");
        if(instances.size() == 0){
            return null;
        }
        String serviceUri = String.format("%S/v1/order/%s",instances.get(0).getUri().toString());
        ResponseEntity<Order> result = restTemplate.exchange(serviceUri, HttpMethod.GET,null,Order.class,orderCode);
        return result.getBody();
    }
}
