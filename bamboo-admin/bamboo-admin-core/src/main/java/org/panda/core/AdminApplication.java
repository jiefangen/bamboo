package org.panda.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application启动类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/4
 **/
@SpringBootApplication
@MapperScan("org.panda.core.modules.system.dao")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class,args);
    }
}
