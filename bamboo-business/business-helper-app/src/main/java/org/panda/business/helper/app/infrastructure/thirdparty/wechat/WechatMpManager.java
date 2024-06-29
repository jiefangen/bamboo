package org.panda.business.helper.app.infrastructure.thirdparty.wechat;

import org.panda.support.openapi.service.WechatMpAccessor;
import org.springframework.stereotype.Component;

/**
 * 微信小程序服务管理器
 *
 * @author fangen
 * @since JDK 11 2024/6/28
 **/
@Component
public class WechatMpManager extends WechatMpAccessor {

    @Override
    public String getAppId() {
        return "wx2b319a3d7edb702d";
    }

    @Override
    protected String getSecret() {
        return "b0a475c1e1d18caf75f9eba4f391723b";
    }

}
