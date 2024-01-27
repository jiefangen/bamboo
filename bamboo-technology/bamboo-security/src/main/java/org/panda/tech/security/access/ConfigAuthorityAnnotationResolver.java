package org.panda.tech.security.access;

import org.panda.tech.security.user.UserConfigAuthority;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * 基于注解的配置权限解决器。一个系统中最多只能有一个实例
 */
public interface ConfigAuthorityAnnotationResolver {

    /**
     * 判断是否支持指定注解
     *
     * @param annotation 注解
     * @return 是否支持指定注解
     */
    boolean supports(@NotNull Annotation annotation);

    /**
     * 解决指定注解在指定请求地址时所需的配置权限集
     *
     * @param annotation 配置权限注解
     * @param url        请求地址
     * @return 配置权限集
     */
    Collection<UserConfigAuthority> resolveConfigAuthorities(@NotNull Annotation annotation, String url);

}
