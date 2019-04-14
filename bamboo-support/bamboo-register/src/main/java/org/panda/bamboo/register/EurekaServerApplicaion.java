package org.panda.bamboo.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 服务注册发现中心
 *
 * @author: jvfagan
 * @since: 2019/3/19
 **/
@EnableEurekaServer
@SpringBootApplication
public class DiscoveryApplicaion {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryApplicaion.class,args);
    }
}
