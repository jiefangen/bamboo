package org.panda.bamboo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 基础订单服务
 *
 * @author jvfagan
 * @date: 2019/04/13
 **/
@SpringBootApplication
@EnableEurekaServer
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
