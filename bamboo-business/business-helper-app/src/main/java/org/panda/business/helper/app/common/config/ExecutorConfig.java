package org.panda.business.helper.app.common.config;

import org.panda.tech.core.concurrent.ExecutorConfigSupport;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步线程池配置
 *
 * @author fangen
 */
@EnableAsync
@Configuration
public class ExecutorConfig extends ExecutorConfigSupport {

    public static final int CORE_POOL_SIZE = 8;

    @Override
    protected int getCorePoolSize() {
        return CORE_POOL_SIZE;
    }
}
