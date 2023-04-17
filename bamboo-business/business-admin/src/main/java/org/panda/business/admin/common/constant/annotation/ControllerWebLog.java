package org.panda.business.admin.common.constant.annotation;

import org.panda.business.admin.common.constant.enumeration.ActionType;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author fangen
 * @since JDK 11 2022/5/6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ControllerWebLog {

    /**
     * 操作日志描述内容
     */
    String content();

    /**
     * 操作日志类型
     */
    ActionType actionType() default ActionType.OTHER;

    /**
     * 是否写入数据库
     */
    boolean intoDb() default false;
}
