package org.panda.business.helper.app;

import org.mybatis.spring.annotation.MapperScan;
import org.panda.tech.auth.AuthModule;
import org.panda.tech.core.CoreModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Application启动类
 *
 * @author fangen
 * @since 2024/6/5
 */
@Import({CoreModule.class, AuthModule.class})
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@MapperScan("org.panda.business.helper.app.repository")
public class HelperAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelperAppApplication.class,args);
    }
}
