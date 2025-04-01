package org.panda.service.notice;

import org.panda.tech.core.CoreModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 通知微服务启动项
 *
 * @author fangen
 */
@Import({CoreModule.class})
@EnableTransactionManagement
@SpringBootApplication
public class NoticeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoticeServiceApplication.class, args);
    }
}
