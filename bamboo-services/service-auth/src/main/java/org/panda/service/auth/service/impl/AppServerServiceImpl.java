package org.panda.service.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.service.auth.infrastructure.security.app.AppServiceModel;
import org.panda.service.auth.infrastructure.security.app.authority.AppConfigAuthority;
import org.panda.service.auth.model.entity.AppServer;
import org.panda.service.auth.model.param.ServiceQueryParam;
import org.panda.service.auth.repository.AppServerMapper;
import org.panda.service.auth.service.AppServerService;
import org.panda.service.auth.service.AuthPermissionService;
import org.panda.service.auth.service.AuthRolePermissionService;
import org.panda.tech.core.config.annotation.GrantAuthority;
import org.panda.tech.core.util.CommonUtil;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.util.QueryPageHelper;
import org.panda.tech.security.cas.CasConstants;
import org.panda.tech.security.user.UserGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * 应用服务 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
@Service
public class AppServerServiceImpl extends ServiceImpl<AppServerMapper, AppServer> implements AppServerService {

    @Autowired
    private AuthPermissionService authPermissionService;
    @Autowired
    private AuthRolePermissionService authRolePermissionService;

    @Override
    public boolean permissionVerification(String service, Collection<? extends GrantedAuthority> grantedAuthorities) {
        if (StringUtils.isBlank(service)) {
            // 尝试从请求头header中获取
            HttpServletRequest request = SpringWebContext.getRequest();
            if (request != null) {
                service = WebHttpUtil.getHeader(request, CasConstants.PARAMETER_SERVICE);
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
        LambdaQueryWrapper<AppServer> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AppServer::getAppName, appName);
        queryWrapper.eq(AppServer::getAppCode, appCode);
        String env = appServiceModel.getEnv();
        queryWrapper.eq(AppServer::getEnv, env);
        AppServer appServer = this.getOne(queryWrapper, false);
        // 组装应用服务参数
        AppServer appServerParam = new AppServer();
        appServerParam.setAppName(appName);
        appServerParam.setAppCode(appCode);
        appServerParam.setEnv(env);
        appServerParam.setHost(appServiceModel.getHost());
        appServerParam.setStatus(1);
        appServerParam.setCaption(appServiceModel.getCaption());
        appServerParam.setBusiness(appServiceModel.getBusiness());
        appServerParam.setScope(appServiceModel.getScope());
        if (appServer == null) { // 应用服务注册激活
            if (this.save(appServerParam)) {
                appServer = this.getOne(Wrappers.lambdaQuery(appServerParam));
            }
        } else { // 已注册过的更新
            appServerParam.setId(appServer.getId());
            this.updateById(appServerParam);
        }
        if (appServer == null) { // 还未获取到应用服务则本次初始化失败
            return appName + " service registration and save failed";
        }
        authRolePermissionService.initPermissions(appServiceModel.getPermissions(), appServer.getId(), appName);
        return Commons.RESULT_SUCCESS;
    }

    @Override
    public QueryResult<AppServer> getServicePage(ServiceQueryParam queryParam) {
        Page<AppServer> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<AppServer> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(queryParam.getKeyword())) {
            queryWrapper.like(AppServer::getAppName, queryParam.getKeyword()).or()
                        .like(AppServer::getAppCode, queryParam.getKeyword());
        }
        IPage<AppServer> servicePage = this.page(page, queryWrapper);
        return QueryPageHelper.convertQueryResult(servicePage);
    }
}
