package org.panda.tech.core.web.mvc.http.annotation;

import java.lang.annotation.*;

/**
 * 结果过滤配置集
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultFilters {

    ResultFilter[] value() default {};

}
