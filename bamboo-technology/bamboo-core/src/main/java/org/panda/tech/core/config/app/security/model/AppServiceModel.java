package org.panda.tech.core.config.app.security.model;

import lombok.Data;
import org.panda.bamboo.common.model.DomainModel;
import org.panda.tech.core.config.app.security.authority.AppConfigAuthority;

import java.util.Collection;
import java.util.List;

/**
 * 应用服务领域模型
 *
 * @author fangen
 **/
@Data
public class AppServiceModel implements DomainModel {
    /**
     * 应用服务名称
     */
    private String appName;
    /**
     * 标题
     */
    private String caption;
    /**
     * 业务
     */
    private String business;
    /**
     * 应用服务范围
     */
    private String scope;

    /**
     * 应用服务权限集
     */
    private List<Permission> permissions;

    @Data
    public static class Permission {
        /**
         * 应用服务api
         */
        private String api;
        /**
         * 资源类型，默认api
         */
        private String resourcesType = "api";
        /**
         * 应用服务api配置权限集合
         */
        private Collection<AppConfigAuthority> appConfigAuthorities;
    }
}
