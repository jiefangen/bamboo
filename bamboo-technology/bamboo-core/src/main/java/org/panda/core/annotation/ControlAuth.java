package org.panda.core.annotation;

import java.lang.annotation.*;

/**
 * 接口权限管控注解
 *
 * @author fangen
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ControlAuth {
    /**
     * 是否需要登录
     */
    boolean needLogin() default true;
}
