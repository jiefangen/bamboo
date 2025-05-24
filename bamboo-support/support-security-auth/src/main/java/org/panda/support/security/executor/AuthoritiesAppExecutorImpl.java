package org.panda.support.security.executor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.bamboo.core.context.SpringContextHolder;
import org.panda.support.security.authority.AppConfigAuthority;
import org.panda.support.security.authority.AuthoritiesAppExecutor;
import org.panda.support.security.executor.strategy.client.AuthServerClient;
import org.panda.support.security.model.AppServiceModel;
import org.panda.tech.core.config.CommonProperties;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.config.app.AppFacade;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

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

    @Value(AppConstants.EL_SERVER_PORT)
    private String port;

    @Autowired
    private CommonProperties commonProperties;
    @Autowired(required = false)
    private AuthServerClient authServerClient;

    @Override
    public void execute() {
        if (this.apiConfigAuthoritiesMapping.isEmpty() || authServerClient == null) {
            return;
        }
        AppFacade appFacade = commonProperties.getAppFacade(appName, true);
        AppServiceModel appServiceModel = new AppServiceModel();
        if (appFacade != null) {
            String contextPath = appFacade.getContextUri();
            if (StringUtils.isEmpty(contextPath)) {
                ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                Environment environment = applicationContext.getEnvironment();
                contextPath = environment.getProperty("server.servlet.context-path");
            }
            appServiceModel.setContextPath(contextPath);
            appServiceModel.setCaption(appFacade.getCaption());
            appServiceModel.setScope(appFacade.getBusiness());
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
            String env = SpringContextHolder.getActiveProfile();
            appServiceModel.setEnv(env);
            appServiceModel.setAppName(appName);
            String host = NetUtil.getLocalHost();
            appServiceModel.setHost(host);
            if (appFacade != null) {
                appServiceModel.setGatewayUri(appFacade.getGatewayUri());
                String directUri = appFacade.getDirectUri();
                if (StringUtils.isEmpty(directUri)) {
                    appServiceModel.setDirectUri(StringUtil.joinObj(NetUtil.PROTOCOL_HTTP, host, Strings.COLON, port,
                            appServiceModel.getContextPath()));
                } else {
                    appServiceModel.setDirectUri(directUri);
                }
            }
            RestfulResult<?> result = authServerClient.authorize(appServiceModel);
            LogUtil.info(getClass(), "{} permissions loading completed, authorize result: {}", appName,
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
