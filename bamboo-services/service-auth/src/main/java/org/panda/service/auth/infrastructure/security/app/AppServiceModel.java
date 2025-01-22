package org.panda.service.auth.infrastructure.security.app;

import lombok.Data;
import org.panda.bamboo.common.model.DomainModel;
import org.panda.service.auth.infrastructure.security.app.authority.AppConfigAuthority;

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
     * 应用运行环境
     */
    private String env;
    /**
     * 运行服务器地址
     */
    private String host;
    /**
     * 标题
     */
    private String caption;
    /**
     * 服务网关URI
     */
    private String gatewayUri;
    /**
     * 服务直连URI
     */
    private String directUri;
    /**
     * 上下文路径
     */
    private String contextPath;
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
