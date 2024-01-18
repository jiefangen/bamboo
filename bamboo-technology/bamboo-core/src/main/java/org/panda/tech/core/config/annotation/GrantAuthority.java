package org.panda.tech.core.config.annotation;

import org.panda.bamboo.common.constant.basic.Strings;

import java.lang.annotation.*;

/**
 * 授予权限。使用该注解标注的调用接口在被调用时授予调用者指定权限。
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GrantAuthority {

    /**
     * @return 授予用户类型
     */
    String type() default Strings.EMPTY;

    /**
     * @return 授予用户级别
     */
    String rank() default Strings.EMPTY;

    /**
     * @return 授予的许可所属应用名称
     */
    String app() default Strings.EMPTY;

    /**
     * @return 授予许可清单
     */
    String[] permission() default {};

    /**
     * @return 授予方式
     */
    Mode mode() default Mode.REPLACEMENT;

    enum Mode {
        /**
         * 无授权时使用
         */
        UNAUTHORIZED,
        /**
         * 替代已有授权
         */
        REPLACEMENT,
        /**
         * 追加到已有授权
         */
        ADDON;
    }

}
