package org.panda.tech.core.annotation.caption;

import java.lang.annotation.*;

/**
 * 说明注解集
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Captions {

    Caption[] value() default {};

}
