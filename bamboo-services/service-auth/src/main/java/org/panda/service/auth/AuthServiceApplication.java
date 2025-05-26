package org.panda.service.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.panda.tech.core.CoreModule;
import org.panda.tech.security.SecurityModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import({CoreModule.class, SecurityModule.class})
@EnableScheduling
@EnableTransactionManagement
@MapperScan("org.panda.service.auth.repository")
@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class,args);
    }
}
