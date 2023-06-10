package org.panda.tech.security.config.annotation;

import org.panda.bamboo.common.constant.basic.Strings;

import java.lang.annotation.*;

/**
 * 配置权限限定。不设置任何属性意味着登录用户均可访问，如果同一个方法上设置多个，意味着用户只需要具有其中任意一个权限，即可访问该方法。
 */
@Documented
@Target(ElementType.METHOD) // 为了尽量避免错误的权限配置造成安全隐患，只能在方法上使用而不能在类上使用，即使这样略显繁琐
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ConfigAuthorities.class)
public @interface ConfigAuthority {

    /**
     * @return 所需用户类型
     */
    String type() default Strings.EMPTY;

    /**
     * 仅当所需用户类型不为空时才有效
     *
     * @return 所需用户级别
     */
    String rank() default Strings.EMPTY;

    /**
     * @return 所需权限所属应用，默认为不限
     */
    String app() default Strings.EMPTY;

    /**
     * @return 所需许可
     */
    String permission() default Strings.EMPTY;

    /**
     * @return 是否只有内网可访问
     */
    boolean intranet() default false;

}
