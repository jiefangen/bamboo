package org.panda.tech.core.web.annotation;

import org.panda.bamboo.common.constant.basic.Strings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内部业务接口标注
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InternalApi {

    /**
     * @return 业务类型，不能包含斜杠/
     */
    String value() default Strings.EMPTY;

}
