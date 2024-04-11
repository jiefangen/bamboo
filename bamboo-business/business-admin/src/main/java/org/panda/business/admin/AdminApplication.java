package org.panda.business.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.panda.tech.core.CoreModule;
import org.panda.tech.core.rpc.annotation.EnableRpcInvoker;
import org.panda.tech.security.SecurityModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Application启动类
 *
 * @author fangen
 */
@Import({CoreModule.class, SecurityModule.class})
@EnableScheduling
@EnableTransactionManagement
@MapperScan("org.panda.business.admin.modules.*.service.repository")
@EnableRpcInvoker // 开启RPC组件调用
@SpringBootApplication
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class,args);
    }
}
