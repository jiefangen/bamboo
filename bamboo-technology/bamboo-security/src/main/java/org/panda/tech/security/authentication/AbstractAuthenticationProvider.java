package org.panda.tech.security.authentication;

import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

/**
 * 抽象的认证提供者
 *
 * @param <A> 认证类型
 */
public abstract class AbstractAuthenticationProvider<A extends Authentication> implements AuthenticationProvider {

    @Override
    public boolean supports(Class<?> authentication) {
        Class<?> genericType = ClassUtil.getActualGenericType(getClass(), 0);
        return genericType != null && genericType.isAssignableFrom(authentication);
    }
}
