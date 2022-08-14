package org.panda.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 接口权限管控注解
 *
 * @author fangen
 * @since 2022/8/14
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Auth {
    /**
     * 是否需要登录
     */
    boolean needLogin() default true;
}
