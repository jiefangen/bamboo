package org.panda.tech.core.spec.enums;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 操作日志类型
 *
 * @author fangen
 */
public enum ActionType {
    @Caption("其它")
    @EnumValue("0")
    OTHER,

    @Caption("新增")
    @EnumValue("1")
    ADD,

    @Caption("删除")
    @EnumValue("2")
    DEL,

    @Caption("查询")
    @EnumValue("3")
    QUERY,

    @Caption("修改")
    @EnumValue("4")
    UPDATE,

    @Caption("登录")
    @EnumValue("5")
    LOGIN,

    @Caption("登出")
    @EnumValue("6")
    QUIT,

    @Caption("授权")
    @EnumValue("7")
    AUTH,

    @Caption("导出")
    @EnumValue("8")
    EXPORT,

    @Caption("导入")
    @EnumValue("9")
    IMPORT,

    @Caption("清空")
    @EnumValue("10")
    EMPTY
}
