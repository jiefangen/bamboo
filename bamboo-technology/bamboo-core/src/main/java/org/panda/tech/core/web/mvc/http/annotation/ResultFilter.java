package org.panda.tech.core.web.mvc.http.annotation;

import java.lang.annotation.*;

/**
 * 结果过滤配置
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ResultFilters.class)
public @interface ResultFilter {
    /**
     * @return 需要过滤的类型，默认为当前结果类型
     */
    Class<?> type() default Object.class;

    /**
     * @return 包含的属性名称集，为空则不限定
     */
    String[] included() default {};

    /**
     * @return 排除的属性名称集，为空则不限定。后续新增属性不会被排除，务必谨慎使用
     */
    String[] excluded() default {};

    /**
     * 默认情况下，枚举属性序列化会添加一个附加属性存放caption，而纯粹的枚举属性不会添加附加属性
     *
     * @return 纯粹的枚举属性名称集
     */
    String[] pureEnum() default {};

}
