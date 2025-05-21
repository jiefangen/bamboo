package org.panda.service.doc;

import org.panda.tech.core.CoreModule;
import org.panda.tech.core.rpc.annotation.EnableRpcInvoker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 文档服务启动项
 *
 * @author fangen
 */
@Import({CoreModule.class})
@EnableTransactionManagement
@EnableRpcInvoker // 开启RPC组件调用
@SpringBootApplication
public class DocServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocServiceApplication.class, args);
    }
}
