package org.panda.tech.core.spec.terminal;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 操作系统类型
 */
public enum OS {

    @Caption("Windows")
    @EnumValue("W")
    WINDOWS,

    @Caption("安卓")
    @EnumValue("A")
    ANDROID,

    @Caption("苹果")
    @EnumValue("M")
    MAC,

    @Caption("Linux")
    @EnumValue("X")
    LINUX,

    @Caption("所有")
    @EnumValue("L")
    ALL;

    public static OS current() {
        String name = System.getProperty("os.name").toUpperCase();
        if (name.contains(WINDOWS.name())) {
            return WINDOWS;
        } else if (name.contains(ANDROID.name())) {
            return ANDROID;
        } else if (name.contains(MAC.name()) || name.contains("IOS")) {
            return MAC;
        }
        return LINUX;
    }

}
