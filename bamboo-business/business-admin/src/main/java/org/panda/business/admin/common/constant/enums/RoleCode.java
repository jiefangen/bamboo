package org.panda.business.admin.common.constant.enums;

import org.panda.bamboo.common.annotation.Caption;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getTopRoles() {
        List<String> topRoles = new ArrayList<>();
        topRoles.add(ADMIN.name());
        topRoles.add(SYSTEM.name());
        topRoles.add(ACTUATOR.name());
        return topRoles;
    }

    public static List<String> getSysRoles() {
        List<String> roles = getTopRoles();
        roles.add(USER.name());
        roles.add(GENERAL.name());
        roles.add(CUSTOMER.name());
        return roles;
    }

    /**
     * 是否是系统角色
     *
     * @param roleCode 角色Code
     * @return true-是；false-否
     */
    public static boolean isSystemRole(String roleCode) {
        return getSysRoles().contains(roleCode);
    }
}
