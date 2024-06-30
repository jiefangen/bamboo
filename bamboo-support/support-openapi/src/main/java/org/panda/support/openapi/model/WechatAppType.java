package org.panda.support.openapi.model;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 微信应用类型
 *
 * @author fangen
 */
public enum WechatAppType {

    @Caption("小程序")
    @EnumValue("MP")
    MP,

    @Caption("公众号")
    @EnumValue("SA")
    SA,

    @Caption("网站")
    @EnumValue("WEB")
    WEB;

}
