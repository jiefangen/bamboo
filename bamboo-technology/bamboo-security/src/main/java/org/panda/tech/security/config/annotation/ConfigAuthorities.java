package org.panda.tech.security.config.annotation;

import java.lang.annotation.*;

/**
 * 配置权限限定集
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigAuthorities {

    ConfigAuthority[] value() default {};

}
