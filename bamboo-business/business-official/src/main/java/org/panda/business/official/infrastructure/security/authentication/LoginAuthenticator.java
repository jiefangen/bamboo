package org.panda.business.official.infrastructure.security.authentication;

import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.tech.security.user.UserSpecificDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 服务端登录认证器
 *
 * @param <T> 令牌类型
 */
public interface LoginAuthenticator<T extends AbstractAuthenticationToken> {

    default Class<T> getTokenType() {
        return ClassUtil.getActualGenericType(getClass(), LoginAuthenticator.class, 0);
    }

    UserSpecificDetails<?> authenticate(String appName, String scope, T token);

}
