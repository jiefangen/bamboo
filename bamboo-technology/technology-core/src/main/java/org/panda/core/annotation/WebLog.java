package org.panda.core.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author fangen
 * @since 2022/5/6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WebLog {

    /**
     * 操作日志描述内容
     */
    String content();

    /**
     * 操作日志类型
     */
    String actionType();

    /**
     * 是否写入数据库
     */
    boolean intoDb() default false;
}
