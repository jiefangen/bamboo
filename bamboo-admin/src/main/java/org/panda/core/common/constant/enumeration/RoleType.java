package org.panda.core.common.constant.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色类型
 *
 * @author jiefangen
 * @since JDK 1.8  2020/6/13
 */
public enum RoleType {
    ADMIN,
    ACTUATOR,
    SYSTEM,
    USER,
    GENERAL,
    CUSTOMER;

    public static List<String> getTopRoles() {
        List<String> list = new ArrayList<>();
        list.add(ADMIN.toString());
        list.add(SYSTEM.toString());
        return list;
    }
}
