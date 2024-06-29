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
    @EnumValue("M")
    MP,

    @Caption("公众号")
    @EnumValue("S")
    SA,

    @Caption("网站")
    @EnumValue("W")
    WEB;

}
