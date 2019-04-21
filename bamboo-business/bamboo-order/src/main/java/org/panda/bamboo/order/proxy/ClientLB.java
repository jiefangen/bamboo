package org.panda.bamboo.order.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端LB
 *
 * @author jvfagan
 * @date: 2019/04/15
 **/
public class ClientLB {
    @Autowired
    private DiscoveryClient discoveryClient;

    public List<String> getEurekaServices(){
        List<String> services = new ArrayList<String>();
        List<String> serviceNames = discoveryClient.getServices();
        for (String serviceName : serviceNames){
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
            for (ServiceInstance instance : serviceInstances){
                serviceNames.add(String.format("%s:%s",serviceName,instance.getUri()));
            }
        }
        return services;
    }
}
