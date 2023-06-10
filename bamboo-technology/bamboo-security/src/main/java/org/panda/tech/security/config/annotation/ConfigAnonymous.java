package org.panda.tech.security.config.annotation;

import org.panda.bamboo.common.constant.basic.Strings;

import java.lang.annotation.*;

/**
 * 配置允许匿名访问，一旦配置，同方法上的@{@link ConfigAuthority}将失效（如果有）
 */
@Documented
@Target(ElementType.METHOD) // 为了尽量避免错误的权限配置造成安全隐患，只能在方法上使用而不能在类上使用，即使这样略显繁琐
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigAnonymous {

    /**
     * @return 当前资源的访问链接的正则表达式。默认为空，框架自动采用Ant通配符形式
     */
    String regex() default Strings.EMPTY;

    /**
     * @return 是否只有内网可访问
     */
    boolean intranet() default false;
}
