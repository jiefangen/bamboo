package org.panda.business.example;

import org.mybatis.spring.annotation.MapperScan;
import org.panda.tech.core.CoreModule;
import org.panda.tech.core.rpc.annotation.EnableRpcInvoker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Import({CoreModule.class})
@EnableTransactionManagement
@EnableRpcInvoker // 开启RPC组件调用
@MapperScan("org.panda.business.official.modules.*.service.repository")
public class ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
