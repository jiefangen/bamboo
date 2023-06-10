package org.panda.tech.security.user;

import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.util.Assert;

/**
 * 要求用户必须具备的权限
 */
public class UserConfigAuthority implements ConfigAttribute {

    private static final long serialVersionUID = 912979753766969750L;

    public static final String SEPARATOR = Strings.PLUS;

    public static final String ATTRIBUTE_DENY_ALL = "denyAll";

    private String type;
    private String rank;
    private String app;
    private String permission;
    /**
     * 是否仅限内网访问
     */
    private boolean intranet;
    /**
     * 是否拒绝所有访问
     */
    private boolean denyAll;

    public UserConfigAuthority(String type, String rank, String app, String permission, boolean intranet) {
        if (type == null) {
            type = Strings.EMPTY;
        }
        Assert.isTrue(!type.contains(SEPARATOR), () -> "The type can not contain '" + SEPARATOR + "'");
        if (rank == null) {
            rank = Strings.EMPTY;
        }
        Assert.isTrue(!rank.contains(SEPARATOR), () -> "The rank can not contain '" + SEPARATOR + "'");
        if (app == null) {
            app = Strings.EMPTY;
        }
        Assert.isTrue(!app.contains(SEPARATOR), () -> "The app can not contain '" + SEPARATOR + "'");
        if (permission == null) {
            permission = Strings.EMPTY;
        }
        Assert.isTrue(!permission.contains(SEPARATOR), () -> "The permission can not contain '" + SEPARATOR + "'");
        this.type = type;
        this.rank = rank;
        this.app = app;
        this.permission = permission;
        this.intranet = intranet;
    }

    /**
     * 构建没有权限限制、登录即可访问的必备权限
     */
    public UserConfigAuthority() {
        this(null, null, null, null, false);
    }

    /**
     * 构建拒绝所有访问的必备权限
     */
    public static UserConfigAuthority ofDenyAll() {
        UserConfigAuthority authority = new UserConfigAuthority();
        authority.denyAll = true;
        return authority;
    }

    public String getType() {
        return this.type;
    }

    public String getRank() {
        return this.rank;
    }

    public String getApp() {
        return this.app;
    }

    public String getPermission() {
        return this.permission;
    }

    public boolean isIntranet() {
        return this.intranet;
    }

    public boolean isDenyAll() {
        return this.denyAll;
    }

    @Override
    public String getAttribute() {
        return this.denyAll ? ATTRIBUTE_DENY_ALL : (this.type + SEPARATOR + this.rank + SEPARATOR + this.app + SEPARATOR + this.permission);
    }

    @Override
    public String toString() {
        return getAttribute();
    }

}
