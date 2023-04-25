package org.panda.business.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Application启动类
 *
 * @author fangen
 * @since JDK 11 2022/4/9
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@MapperScan("org.panda.business.admin.modules.*")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class,args);
    }
}
