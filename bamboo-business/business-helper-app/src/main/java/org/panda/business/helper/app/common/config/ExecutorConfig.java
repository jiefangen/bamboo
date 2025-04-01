package org.panda.business.helper.app.common.config;

import org.panda.tech.core.concurrent.ExecutorConfigSupport;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 系统全局线程池配置
 *
 * @author fangen
 */
@EnableAsync
@Configuration
public class ExecutorConfig extends ExecutorConfigSupport {
    /**
     * 服务可支持的最佳线程池核心数
     * 2核心4G的服务器配置
     */
    public static final int CORE_POOL_SIZE = 4;

    @Override
    protected int getCorePoolSize() {
        return CORE_POOL_SIZE;
    }
}
