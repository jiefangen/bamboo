package org.panda.tech.data.annotation;

import org.panda.tech.data.common.DataCommons;

import java.lang.annotation.*;

/**
 * 数据源切换注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataSourceSwitch {

    String value() default DataCommons.DATASOURCE_PRIMARY;
}
