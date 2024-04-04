package org.panda.service.payment;

import org.panda.tech.core.CoreModule;
import org.panda.tech.core.config.annotation.EnableAuthServer;
import org.panda.tech.core.rpc.annotation.EnableRpcInvoker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 支付微服务启动项
 *
 * @author fangen
 */
@Import({CoreModule.class})
@EnableTransactionManagement
@EnableAuthServer // 开启服务的认证鉴权
@EnableRpcInvoker // 开启RPC组件调用
@SpringBootApplication
public class PaymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
