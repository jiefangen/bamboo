package org.panda.tech.core.spec.terminal;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 程序类型
 */
public enum Program {

    @Caption("网页")
    @EnumValue("W")
    WEB,

    @Caption("原生")
    @EnumValue("N")
    NATIVE,

    @Caption("小程序")
    @EnumValue("M")
    MINI,

    @Caption("原生内嵌网页")
    @EnumValue("H")
    HYBRID,

}
