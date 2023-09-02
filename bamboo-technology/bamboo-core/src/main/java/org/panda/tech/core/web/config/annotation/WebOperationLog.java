package org.panda.tech.core.web.config.annotation;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.spec.log.ActionType;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author fangen
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebOperationLog {
    /**
     * 操作日志描述内容
     */
    String content() default Strings.EMPTY;
    /**
     * 操作日志类型
     */
    ActionType actionType() default ActionType.OTHER;
    /**
     * 是否持久化存储
     */
    boolean intoStorage() default false;
}
