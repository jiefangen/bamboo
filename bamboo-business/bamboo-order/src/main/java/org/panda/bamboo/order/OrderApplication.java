package org.panda.bamboo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * 基础订单服务
 *
 * @author jvfagan
 * @date: 2019/04/13
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
//@EnableResourceServer
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
