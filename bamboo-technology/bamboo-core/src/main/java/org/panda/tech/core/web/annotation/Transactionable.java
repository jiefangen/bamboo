package org.panda.tech.core.web.annotation;

import java.lang.annotation.*;

/**
 * 可具有事务的
 *
 * @author jianglei
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transactionable {

    /**
     *
     * @return 代理接口类型
     */
    Class<?>[] proxyInterface();

    /**
     *
     * @return 将具有只读事务的方法名称式样
     */
    String[] read() default {};

    /**
     *
     * @return 将具有可写事务的方法名称式样
     */
    String[] write() default {};
}
