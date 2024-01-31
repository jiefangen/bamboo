package org.panda.business.admin.common.constant.enums;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 系统全局用户类型
 */
public enum UserType {

    @Caption("管理员")
    @EnumValue("manager")
    MANAGER,

    @Caption("普通用户")
    @EnumValue("general")
    GENERAL,

    @Caption("访客")
    @EnumValue("customer")
    CUSTOMER;

}
