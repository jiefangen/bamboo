package org.panda.bamboo.common.annotation;

import org.panda.bamboo.common.constant.basic.Strings;

import java.lang.annotation.*;

/**
 * 说明注解，用于注释、说明、解释
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Captions.class)
public @interface Caption {
    /**
     * 说明文本
     *
     * @return 说明文本
     */
    String value();

    String locale() default Strings.EMPTY;

}
