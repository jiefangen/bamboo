package org.panda.service.auth.common.constant.enums;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 服务状态
 */
public enum ServiceStatus {

    @Caption("故障")
    @EnumValue("0")
    DOWN,

    @Caption("正常")
    @EnumValue("1")
    UP,

    @Caption("维护中")
    @EnumValue("2")
    MAINTENANCE;
}
