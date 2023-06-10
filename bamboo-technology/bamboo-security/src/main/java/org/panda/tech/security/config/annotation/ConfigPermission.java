package org.panda.tech.security.config.annotation;

import org.panda.bamboo.common.constant.basic.Strings;

import java.lang.annotation.*;

/**
 * 配置许可限定。{@link ConfigAuthority}的替代者，替代其中的许可限定为默认许可名称的限定，其它限定不变
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ConfigPermissions.class)
public @interface ConfigPermission {

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
     * @return 是否只有内网可访问
     */
    boolean intranet() default false;

}
