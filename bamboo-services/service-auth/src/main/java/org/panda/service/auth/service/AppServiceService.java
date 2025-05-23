package org.panda.service.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.service.auth.infrastructure.security.app.AppServiceModel;
import org.panda.service.auth.model.entity.AppService;
import org.panda.service.auth.model.param.ServiceQueryParam;
import org.panda.service.auth.model.vo.ServiceNodeVO;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <p>
 * 应用服务 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2025-01-21
 */
public interface AppServiceService extends IService<AppService> {
    /**
     * 服务权限验证
     *
     * @param service 服务资源
     * @param grantedAuthorities 访问者权限集
     * @return 验证结果
     */
    boolean permissionVerification(String service, Collection<? extends GrantedAuthority> grantedAuthorities);

    /**
     * 应用服务资源授权
     *
     * @param appServiceModel 应用服务资源模型
     * @return 授权结果
     */
    String initServicePermission(AppServiceModel appServiceModel);

    /**
     * 获取服务信息
     *
     * @param queryParam 查询参数
     * @return 结果
     */
    QueryResult<AppService> getServicePage(ServiceQueryParam queryParam);

    /**
     * 服务节点健康检测
     */
    void checkServiceHealth();

    /**
     * 获取服务节点信息
     *
     * @param appName 应用名称
     * @return 节点信息
     */
    ServiceNodeVO getServiceNodes(String appName);
}
