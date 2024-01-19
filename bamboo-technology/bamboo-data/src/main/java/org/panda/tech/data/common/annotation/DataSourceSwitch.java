package org.panda.tech.data.common.annotation;

import org.panda.tech.data.common.DataCommons;

import java.lang.annotation.*;

/**
 * 数据源切换注解
 * 为了尽量控制数据源切换范围，只能在方法上使用而不能在类上使用，并且需要在Spring管理的类中使用
 * 数据源切换范围过大会影响到类方法体中不同数据源的使用粒度
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataSourceSwitch {

    String value() default DataCommons.DATASOURCE_PRIMARY;
}
