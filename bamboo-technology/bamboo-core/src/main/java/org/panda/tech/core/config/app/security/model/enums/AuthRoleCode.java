package org.panda.tech.core.config.app.security.model.enums;

import org.panda.bamboo.common.annotation.Caption;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务鉴权角色编码
 */
public enum AuthRoleCode {

    @Caption("顶级管理员")
    ADMIN,

    @Caption("账户角色权限")
    ACCOUNT,

    @Caption("访客")
    CUSTOMER;


    public static List<String> getManagerRoles() {
        List<String> topRoles = new ArrayList<>();
        topRoles.add(ADMIN.name());
        return topRoles;
    }

    /**
     * 是否是管理员角色
     *
     * @param roleCode 角色Code
     * @return true-是；false-否
     */
    public static boolean isManagerRole(String roleCode) {
        return getManagerRoles().contains(roleCode);
    }
}
