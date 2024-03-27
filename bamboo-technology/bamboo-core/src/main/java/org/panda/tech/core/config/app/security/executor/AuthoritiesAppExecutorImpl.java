package org.panda.tech.core.config.app.security.executor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.config.CommonProperties;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.config.app.AppFacade;
import org.panda.tech.core.config.app.security.authority.AppConfigAuthority;
import org.panda.tech.core.config.app.security.authority.AuthoritiesAppExecutor;
import org.panda.tech.core.config.app.security.executor.strategy.client.AuthServerClient;
import org.panda.tech.core.config.app.security.model.AppServiceModel;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * 应用权限集业务扩展执行器实现
 *
 * @author fangen
 **/
public class AuthoritiesAppExecutorImpl implements AuthoritiesAppExecutor {
    /**
     * 映射集：api路径-所需权限清单
     */
    private final Map<String, Collection<AppConfigAuthority>> apiConfigAuthoritiesMapping = new HashMap<>();

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    @Autowired
    private CommonProperties commonProperties;
//    @Autowired
    private AuthServerClient authServerClient;

    @Override
    public void execute() {
        if (this.apiConfigAuthoritiesMapping.isEmpty()) {
            return;
        }
        AppFacade appFacade = commonProperties.getAppFacade(appName, true);
        AppServiceModel appServiceModel = new AppServiceModel();
        appServiceModel.setAppName(appName);
        if (appFacade != null) {
            appServiceModel.setCaption(appFacade.getCaption());
            appServiceModel.setBusiness(appFacade.getBusiness());
        }
        List<AppServiceModel.Permission> permissions = new ArrayList<>();
        for (Map.Entry<String, Collection<AppConfigAuthority>> authorityEntry : this.apiConfigAuthoritiesMapping.entrySet()) {
            Collection<AppConfigAuthority> appConfigAuthorities = authorityEntry.getValue();
            AppServiceModel.Permission permission = new AppServiceModel.Permission();
            permission.setApi(authorityEntry.getKey());
            permission.setAppConfigAuthorities(appConfigAuthorities);
            permissions.add(permission);
        }
        appServiceModel.setPermissions(permissions);
        try {
            RestfulResult<?> result = authServerClient.authorize(appServiceModel);
            LogUtil.info(getClass(), "{} service permissions loading completed, authorize result: {}", appName,
                    JsonUtil.toJson(result));
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
    }

    @Override
    public void setApiConfigAuthoritiesMapping(String api, Collection<AppConfigAuthority> authorities) {
        if (StringUtils.isNotBlank(api)) {
            this.apiConfigAuthoritiesMapping.put(api, authorities);
        }
    }

    @Override
    public String[] getUrlPatterns() {
        List<String> authUrlPatterns = commonProperties.getAuthUrlPatterns();
        if (CollectionUtils.isNotEmpty(authUrlPatterns)) {
            return authUrlPatterns.toArray(new String[0]);
        }
        return new String[]{};
    }
}
