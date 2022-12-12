package org.panda.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 认证服务器
 *
 * @author jvfagan
 * @since JDK 1.8  2019/5/7
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableAuthorizationServer
public class AuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class,args);
    }

}
