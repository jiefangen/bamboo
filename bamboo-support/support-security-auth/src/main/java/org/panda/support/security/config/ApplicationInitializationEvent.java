package org.panda.support.security.config;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.bamboo.core.context.SpringContextHolder;
import org.panda.support.security.executor.strategy.client.AuthServerClient;
import org.panda.support.security.model.AppServiceModel;
import org.panda.tech.core.boot.ApplicationContextDelayRunner;
import org.panda.tech.core.config.CommonProperties;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.config.app.AppFacade;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 服务授权注册事件
 *
 * @author fangen
 **/
@Component
public class ApplicationInitializationEvent implements ApplicationContextDelayRunner {

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;
    @Value(AppConstants.EL_SERVER_PORT)
    private String port;

    @Autowired
    private CommonProperties commonProperties;
    @Autowired(required = false)
    private AuthServerClient authServerClient;

    @Override
    public int getOrder() {
        return ApplicationContextDelayRunner.super.getOrder();
    }

    @Override
    public void run(ApplicationContext context) throws Exception {
        // 应用服务信息初始化
        RestfulResult<?> result = initService();
        if (result.isSuccess()) {
            LogUtil.info(getClass(), "Initialize the application service result: {}", JsonUtil.toJson(result));
        } else {
            LogUtil.warn(getClass(), "Initialize the application service result: {}", JsonUtil.toJson(result));
        }
    }

    private RestfulResult<?> initService() {
        if (authServerClient == null) {
            return RestfulResult.failure("AuthServerClient not configured.");
        }
        AppFacade appFacade = commonProperties.getAppFacade(appName, true);
        AppServiceModel appServiceModel = new AppServiceModel();
        String env = SpringContextHolder.getActiveProfile();
        appServiceModel.setEnv(env);
        appServiceModel.setAppName(appName);
        String host = NetUtil.getLocalHost();
        appServiceModel.setHost(host);
        // 应用门户信息
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
            // 应用访问地址
            appServiceModel.setGatewayUri(appFacade.getGatewayUri());
            String directUri = appFacade.getDirectUri();
            if (StringUtils.isEmpty(directUri)) {
                appServiceModel.setDirectUri(StringUtil.joinObj(NetUtil.PROTOCOL_HTTP, host, Strings.COLON, port,
                        appServiceModel.getContextPath()));
            } else {
                appServiceModel.setDirectUri(directUri);
            }
        }
        return authServerClient.authorize(appServiceModel);
    }
}
