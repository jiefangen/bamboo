package org.panda.service.auth.infrastructure.security.app.enums;

import org.panda.bamboo.common.annotation.Caption;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务鉴权角色编码
 */
public enum AuthRoleCode {

    @Caption("管理员")
    MANAGER,

    @Caption("账户角色")
    ACCOUNT,

    @Caption("访客")
    CUSTOMER;


    public static List<String> getTopRoles() {
        List<String> topRoles = new ArrayList<>();
        topRoles.add(MANAGER.name());
        topRoles.add(ACCOUNT.name());
        return topRoles;
    }

    /**
     * 是否是顶级权限角色
     *
     * @param roleCode 角色Code
     * @return true-是；false-否
     */
    public static boolean isTopRole(String roleCode) {
        return getTopRoles().contains(roleCode);
    }
}
