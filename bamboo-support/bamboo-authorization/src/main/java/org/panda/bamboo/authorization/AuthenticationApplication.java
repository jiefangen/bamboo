package org.panda.bamboo.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证服务器
 *
 * @author jvfagan
 * @since JDK 1.8  2019/5/7
 **/
@SpringBootApplication
@RestController
@EnableEurekaClient
@EnableAuthorizationServer
public class AuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class,args);
    }

}
