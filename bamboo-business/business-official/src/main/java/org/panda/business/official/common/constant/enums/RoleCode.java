package org.panda.business.official.common.constant.enums;

import org.panda.bamboo.common.annotation.Caption;

/**
 * 系统全局角色Code
 */
public enum RoleCode {

    @Caption("顶级管理员")
    ADMIN,

    @Caption("系统管理员")
    SYSTEM,

    @Caption("监控管理员")
    ACTUATOR,

    @Caption("特性用户")
    USER,

    @Caption("普通用户")
    GENERAL,

    @Caption("访客")
    CUSTOMER;

}
