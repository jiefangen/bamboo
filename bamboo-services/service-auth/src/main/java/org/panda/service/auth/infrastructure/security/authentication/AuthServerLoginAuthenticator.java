package org.panda.service.auth.infrastructure.security.authentication;

import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.tech.security.user.UserSpecificDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Auth服务端登录认证器
 *
 * @param <T> 令牌类型
 */
public interface AuthServerLoginAuthenticator<T extends AbstractAuthenticationToken> {

    default Class<T> getTokenType() {
        return ClassUtil.getActualGenericType(getClass(), AuthServerLoginAuthenticator.class, 0);
    }

    UserSpecificDetails<?> authenticate(String appName, String scope, T token);

}
