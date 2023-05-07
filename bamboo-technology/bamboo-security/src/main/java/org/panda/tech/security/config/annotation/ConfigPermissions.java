package org.panda.tech.security.config.annotation;

import java.lang.annotation.*;

/**
 * 配置许可限定集
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigPermissions {

    ConfigPermission[] value() default {};

}
