package org.panda.service.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.clazz.ClassParse;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.service.auth.common.constant.enums.ServiceStatus;
import org.panda.service.auth.infrastructure.security.app.AppServiceModel;
import org.panda.service.auth.infrastructure.security.app.authority.AppConfigAuthority;
import org.panda.service.auth.model.entity.AppService;
import org.panda.service.auth.model.entity.AppServiceNode;
import org.panda.service.auth.model.param.ServiceQueryParam;
import org.panda.service.auth.model.vo.ServiceNodeVO;
import org.panda.service.auth.repository.AppServiceMapper;
import org.panda.service.auth.service.AppServiceNodeService;
import org.panda.service.auth.service.AppServiceService;
import org.panda.service.auth.service.AuthPermissionService;
import org.panda.service.auth.service.AuthRolePermissionService;
import org.panda.tech.core.config.annotation.GrantAuthority;
import org.panda.tech.core.util.CommonUtil;
import org.panda.tech.core.util.http.client.RestTemplateClient;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.util.QueryPageHelper;
import org.panda.tech.security.config.constants.SecurityConstants;
import org.panda.tech.security.user.UserGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 应用服务 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2025-01-21
 */
@Service
public class AppServiceServiceImpl extends ServiceImpl<AppServiceMapper, AppService> implements AppServiceService {

    @Autowired
    private AuthPermissionService authPermissionService;
    @Autowired
    private AuthRolePermissionService authRolePermissionService;
    @Autowired
    private AppServiceNodeService appServiceNodeService;

    @Override
    public boolean permissionVerification(String service, Collection<? extends GrantedAuthority> grantedAuthorities) {
        if (StringUtils.isBlank(service)) {
            // 尝试从请求头header中获取
            HttpServletRequest request = SpringWebContext.getRequest();
            if (request != null) {
                service = WebHttpUtil.getHeader(request, SecurityConstants.PARAMETER_SERVICE);
            }
        }
        if (StringUtils.isNotBlank(service)) {
            // 匿名资源只要登录就能调用
            String anonymousScope = new AppConfigAuthority().toString(); // 匿名规则1
            String anonymousDesc = GrantAuthority.Mode.UNAUTHORIZED.name(); // 匿名规则2
            Set<String> anonymousPermissions = authPermissionService.getAnonymousPermission(anonymousScope, anonymousDesc);
            boolean retBool = false;
            if (CollectionUtils.isNotEmpty(anonymousPermissions)) {
                retBool = isGranted(anonymousPermissions, service);
            }
            if (retBool) {
                return true;
            } else {
                for (GrantedAuthority grantedAuthority : grantedAuthorities) {
                    if (grantedAuthority instanceof UserGrantedAuthority) {
                        UserGrantedAuthority userGrantedAuthority = (UserGrantedAuthority) grantedAuthority;
                        Set<String> roleCodes = userGrantedAuthority.getPermissions();
                        if (CollectionUtils.isNotEmpty(roleCodes)) {
                            Set<String> permissions = authPermissionService.getPermissions(roleCodes);
                            if (CollectionUtils.isNotEmpty(permissions)) {
                                // 服务授权鉴定
                                return isGranted(permissions, service);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isGranted(Set<String> permissions, String service) {
        String apiCode = CommonUtil.getDefaultPermission(service).toUpperCase();
        return StringUtil.antPathMatchOneOf(apiCode, permissions);
    }

    @Override
    public String initServicePermission(AppServiceModel appServiceModel) {
        String appName = appServiceModel.getAppName();
        String appCode = appName.toUpperCase();
        LambdaQueryWrapper<AppService> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AppService::getAppCode, appCode);
        String env = appServiceModel.getEnv();
        appName += Strings.MINUS + env;
        queryWrapper.eq(AppService::getAppName, appName);
        queryWrapper.eq(AppService::getEnv, env);
        AppService appService = this.getOne(queryWrapper, false);
        // 应用服务注册激活
        if (appService == null) {
            // 组装应用服务参数
            AppService appServiceParam = new AppService();
            appServiceParam.setAppName(appName);
            appServiceParam.setAppCode(appCode);
            appServiceParam.setEnv(env);
            appServiceParam.setStatus(ClassParse.visit(EnumValueHelper.getValue(ServiceStatus.UP), Integer.class));
            appServiceParam.setCaption(appServiceModel.getCaption());
            appServiceParam.setGatewayUri(appServiceModel.getGatewayUri());
            appServiceParam.setContextPath(appServiceModel.getContextPath());
            appServiceParam.setScope(appServiceModel.getScope());
            if (this.save(appServiceParam)) {
                appService = this.getOne(Wrappers.lambdaQuery(appServiceParam));
                // 添加服务节点
                addServiceNode(appService.getId(), appName, appServiceModel.getHost(), appServiceModel.getDirectUri());
            }
        } else { // 已注册过的更新
            appService.setCaption(appServiceModel.getCaption());
            appService.setGatewayUri(appServiceModel.getGatewayUri());
            appService.setContextPath(appServiceModel.getContextPath());
            appService.setScope(appServiceModel.getScope());
            this.updateById(appService);
            // 集群环境下添加服务多节点
            addServiceNode(appService.getId(), appName, appServiceModel.getHost(), appServiceModel.getDirectUri());
        }
        if (appService == null) { // 还未获取到应用服务则本次初始化失败
            return appName + " service registration and save failed";
        }
        if (CollectionUtils.isNotEmpty(appServiceModel.getPermissions())) {
            authRolePermissionService.initPermissions(appServiceModel.getPermissions(), appService.getId(), appName);
        }
        return Commons.RESULT_SUCCESS;
    }

    private void addServiceNode(Integer serviceId, String appName, String host, String directUri) {
        LambdaQueryWrapper<AppServiceNode> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AppServiceNode::getServiceId, serviceId);
        queryWrapper.eq(AppServiceNode::getAppName, appName);
        queryWrapper.eq(AppServiceNode::getHost, host);
        AppServiceNode appServiceNode = appServiceNodeService.getOne(queryWrapper, false);
        Integer upStatus = ClassParse.visit(EnumValueHelper.getValue(ServiceStatus.UP), Integer.class);
        if (appServiceNode == null) {
            AppServiceNode appServiceNodeParam = new AppServiceNode();
            appServiceNodeParam.setServiceId(serviceId);
            appServiceNodeParam.setAppName(appName);
            appServiceNodeParam.setStatus(upStatus);
            appServiceNodeParam.setHost(host);
            appServiceNodeParam.setDirectUri(directUri);
            appServiceNodeService.save(appServiceNodeParam);
        } else {
            appServiceNode.setDirectUri(directUri);
            appServiceNode.setStatus(upStatus);
            appServiceNode.setUpdateTime(LocalDateTime.now());
            appServiceNodeService.updateById(appServiceNode);
        }
    }

    @Override
    public QueryResult<AppService> getServicePage(ServiceQueryParam queryParam) {
        Page<AppService> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<AppService> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(queryParam.getKeyword())) {
            queryWrapper.like(AppService::getAppName, queryParam.getKeyword()).or()
                    .like(AppService::getAppCode, queryParam.getKeyword());
        }
        IPage<AppService> servicePage = this.page(page, queryWrapper);
        return QueryPageHelper.convertQueryResult(servicePage);
    }

    @Override
    public void checkServiceHealth() {
        List<AppService> serviceList = this.list();
        if (CollectionUtils.isNotEmpty(serviceList)) {
            for (AppService appService : serviceList) {
                int serviceStatus = appService.getStatus();
                LambdaQueryWrapper<AppServiceNode> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(AppServiceNode::getServiceId, appService.getId());
                queryWrapper.eq(AppServiceNode::getAppName, appService.getAppName());
                List<AppServiceNode> appServiceNodes = appServiceNodeService.list(queryWrapper);
                if (CollectionUtils.isNotEmpty(appServiceNodes)) {
                    int serviceNodeNormalCount = 0; // 正常服务节点统计
                    // 服务节点检测
                    for (AppServiceNode appServiceNode : appServiceNodes) {
                        String uri = appServiceNode.getDirectUri();
                        int serviceNodeStatus = 0;
                        try {
                            String reqResult = RestTemplateClient.requestByGet(uri + "/home");
                            if (StringUtils.isNotEmpty(reqResult)) {
                                RestfulResult<?> result = JsonUtil.json2Bean(reqResult, RestfulResult.class);
                                if (result != null) {
                                    serviceNodeStatus = result.isSuccess()?1:0;
                                }
                            }
                        } catch (Exception e) {
                            // do nothing
                        }
                        appServiceNode.setStatus(serviceNodeStatus);
                        appServiceNode.setUpdateTime(LocalDateTime.now());
                        appServiceNodeService.updateById(appServiceNode);
                        if (serviceNodeStatus == 1) {
                            serviceNodeNormalCount++;
                        }
                    }

                    // 主服务状态判定
                    if (serviceNodeNormalCount == appServiceNodes.size()) { // 所有服务节点都正常
                        serviceStatus = 1;
                    } else if (serviceNodeNormalCount == 0) { // 所有服务节点都故障
                        serviceStatus = 0;
                    } else { // 部分服务节点正常
                        serviceStatus = 2;
                    }
                }
                // 更新服务主表状态
                appService.setStatus(serviceStatus);
                this.updateById(appService);
            }
        }
    }

    @Override
    public ServiceNodeVO getServiceNodes(String appName) {
        LambdaQueryWrapper<AppService> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AppService::getAppName, appName);
        // 服务状态不等于0的查询结果
        queryWrapper.ne(AppService::getStatus, 0);
        AppService appService = this.getOne(queryWrapper, false);
        ServiceNodeVO serviceNodeVO = null;
        if (appService != null) {
            serviceNodeVO = JsonUtil.json2Bean(JsonUtil.toJson(appService), ServiceNodeVO.class);
            // 查询服务节点注册信息
            LambdaQueryWrapper<AppServiceNode> serviceNodWrapper = Wrappers.lambdaQuery();
            serviceNodWrapper.eq(AppServiceNode::getServiceId, appService.getId());
            serviceNodWrapper.eq(AppServiceNode::getAppName, appService.getAppName());
            serviceNodWrapper.eq(AppServiceNode::getStatus, 1);
            List<AppServiceNode> appServiceNodes = appServiceNodeService.list(serviceNodWrapper);
            if (CollectionUtils.isNotEmpty(appServiceNodes) && serviceNodeVO != null) {
                List<String> directUris = appServiceNodes.stream().map(AppServiceNode::getDirectUri).collect(Collectors.toList());
                serviceNodeVO.setDirectUris(directUris);
            }
        }
        return serviceNodeVO;
    }
}
