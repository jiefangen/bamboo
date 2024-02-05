package org.panda.service.auth.common.constant.enums;

import org.panda.bamboo.common.annotation.Caption;

/**
 * 账户类型
 */
public enum AccountType {

    @Caption("管理员")
    MANAGER,

    @Caption("系统用户")
    SYSTEM,

    @Caption("普通用户")
    GENERAL,

    @Caption("访客")
    CUSTOMER;

}
