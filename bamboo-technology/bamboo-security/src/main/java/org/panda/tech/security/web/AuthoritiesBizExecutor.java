package org.panda.tech.security.web;

import org.panda.tech.security.user.UserConfigAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * 权限集业务扩展执行器
 */
public interface AuthoritiesBizExecutor {
    /**
     * 业务扩展执行，交由上层业务实现
     */
    default void execute(String api, Collection<UserConfigAuthority> authorities) {
    }

    default Map<String, Collection<UserConfigAuthority>> getApiConfigAuthoritiesMapping(){
        return null;
    }
}
