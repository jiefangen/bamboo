package org.panda.ms.doc;

import org.panda.tech.core.CoreModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * 文档微服务启动项
 *
 * @author fangen
 */
@SpringBootApplication
@Import({CoreModule.class})
public class DocServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocServiceApplication.class, args);
    }
}
