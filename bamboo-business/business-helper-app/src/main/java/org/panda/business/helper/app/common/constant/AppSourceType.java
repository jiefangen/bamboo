package org.panda.business.helper.app.common.constant;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * APP来源类型
 */
public enum AppSourceType {

    @Caption("微信小程序")
    @EnumValue("WECHAT-MINI")
    WECHAT_MINI,

    @Caption("安卓")
    @EnumValue("ANDROID")
    ANDROID,

    @Caption("苹果")
    @EnumValue("IOS")
    IOS,

}
