package org.panda.business.helper.app.infrastructure.thirdparty.wechat;

import org.panda.bamboo.common.constant.Profiles;
import org.panda.support.openapi.service.WechatMpAccessor;
import org.panda.tech.core.config.app.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信小程序服务管理器
 *
 * @author fangen
 * @since JDK 11 2024/6/28
 **/
@Component
public class WechatMpManager extends WechatMpAccessor {
    /**
     * 生产环境APPID和密钥
     */
    private static final String APP_ID_WECHAT_PROD = "wx14ba20197d75e130";
    private static final String SECRET_WECHAT_PROD = "e5e06e946081372c920d1b47c3ca0f98";
    /**
     * 测试环境APPID和密钥
     */
    private static final String APP_ID_WECHAT_TEST = "wx2b319a3d7edb702d";
    private static final String SECRET_WECHAT_TEST = "b0a475c1e1d18caf75f9eba4f391723b";

    @Value(AppConstants.EL_SPRING_PROFILES_ACTIVE)
    private String env;

    @Override
    public String getAppId() {
        if (Profiles.PRODUCTION.equals(env)) {
            return APP_ID_WECHAT_PROD;
        } else {
            return APP_ID_WECHAT_TEST;
        }
    }

    @Override
    protected String getSecret() {
        if (Profiles.PRODUCTION.equals(env)) {
            return SECRET_WECHAT_PROD;
        } else {
            return SECRET_WECHAT_TEST;
        }
    }
}
