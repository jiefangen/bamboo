package org.panda.tech.core.config.app.security.authority;

import java.util.Collection;

/**
 * 应用权限集业务扩展执行器
 */
public interface AuthoritiesAppExecutor {
    /**
     * 业务扩展执行，交由上层业务实现
     */
    void execute();
    /**
     * 加载api级的权限限定容器
     *
     * @param api api路径
     * @param authorities 权限限定集
     */
    default void setApiConfigAuthoritiesMapping(String api, Collection<AppConfigAuthority> authorities) {
    }
    /**
     * 获取业务扩展拦截url规则
     */
    default String[] getUrlPatterns(){
        return new String[0];
    }
}
