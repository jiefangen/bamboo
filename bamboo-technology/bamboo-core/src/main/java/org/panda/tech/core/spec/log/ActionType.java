package org.panda.tech.core.spec.log;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 操作日志类型
 *
 * @author fangen
 */
public enum ActionType {
    @Caption("新增")
    @EnumValue("add")
    ADD,

    @Caption("删除")
    @EnumValue("del")
    DEL,

    @Caption("查询")
    @EnumValue("query")
    QUERY,

    @Caption("修改")
    @EnumValue("update")
    UPDATE,

    @Caption("登录")
    @EnumValue("login")
    LOGIN,

    @Caption("登出")
    @EnumValue("quit")
    QUIT,

    @Caption("授权")
    @EnumValue("auth")
    AUTH,

    @Caption("导出")
    @EnumValue("export")
    EXPORT,

    @Caption("导入")
    @EnumValue("import")
    IMPORT,

    @Caption("清空")
    @EnumValue("empty")
    EMPTY,

    @Caption("其它")
    @EnumValue("other")
    OTHER,
}
