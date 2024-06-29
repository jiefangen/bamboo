package org.panda.support.openapi.service;

import org.panda.support.openapi.model.WechatAppType;
import org.panda.support.openapi.service.support.WechatOpenAppAccessSupport;

/**
 * 微信Web应用访问器
 */
public abstract class WechatWebAccessor extends WechatOpenAppAccessSupport {

    @Override
    public final WechatAppType getAppType() {
        return WechatAppType.WEB;
    }

}
