package org.panda.tech.security.model;

import org.panda.tech.core.annotation.caption.Caption;

/**
 * 已取得权限的性质
 */
public enum GrantedAuthorityKind {

    @Caption("类型")
    TYPE,

    @Caption("级别")
    RANK,

    @Caption("许可")
    PERMISSION;

}
