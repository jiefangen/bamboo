package org.panda.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author jvfagan
 * @version: 1.0 2019/4/25
 * @since JDK 1.8
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {

        SpringApplication.run(ConfigServerApplication.class,args);
    }
}
