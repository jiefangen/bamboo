package org.panda.business.official;

import org.mybatis.spring.annotation.MapperScan;
import org.panda.tech.core.CoreModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Import({CoreModule.class})
@EnableTransactionManagement
@MapperScan("org.panda.business.official.data.repository")
public class OfficialApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfficialApplication.class, args);
    }
}
