package org.panda.business.admin.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程池配置
 *
 * @author fangen
 */
@EnableAsync
@Configuration
public class AsyncTaskConfig {
    /**
     * 声明一个线程池(并指定线程池的名字)
     */
    @Bean("taskExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数10：线程池创建时候初始化的线程数
        executor.setCorePoolSize(10);
        // 最大线程数20：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(20);
        // 缓冲队列100：用来缓冲执行任务的队列
        executor.setQueueCapacity(100);
        // 允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix("my-async-task-");

        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 声明一个线程池(并指定线程池的名字)
     */
    @Bean("scheduledExecutor")
    public Executor scheduledExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数10：线程池创建时候初始化的线程数
        executor.setCorePoolSize(10);
        // 最大线程数20：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(20);
        // 缓冲队列100：用来缓冲执行任务的队列
        executor.setQueueCapacity(100);
        // 允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix("my-scheduled-");

        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
