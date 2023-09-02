package org.panda.tech.core.spec.terminal;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 设备类型
 */
public enum Device {

    @Caption("电脑")
    @EnumValue("C")
    PC,

    @Caption("手机")
    @EnumValue("H")
    PHONE,

    @Caption("平板")
    @EnumValue("D")
    PAD,
}
