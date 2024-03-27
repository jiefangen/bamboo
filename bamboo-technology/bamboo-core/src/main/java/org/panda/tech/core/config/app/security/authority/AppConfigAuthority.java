package org.panda.tech.core.config.app.security.authority;

import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.util.Assert;

/**
 * 要求应用必须具备的权限
 */
public class AppConfigAuthority implements ConfigAttribute {

    private static final long serialVersionUID = -5087737434098808197L;

    public static final String SEPARATOR = Strings.PLUS;

    public static final String ATTRIBUTE_DENY_ALL = "denyAll";

    private String type;
    private String rank;
    private String app;
    private String permission;
    private String mode;
    /**
     * 是否拒绝所有访问
     */
    private boolean denyAll;

    public AppConfigAuthority(String type, String rank, String app, String permission, String mode) {
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
        this.mode = mode;
    }

    /**
     * 构建没有权限限制、登录即可访问的必备权限
     */
    public AppConfigAuthority() {
        this(null, null, null, null, null);
    }

    /**
     * 构建拒绝所有访问的必备权限
     */
    public static AppConfigAuthority ofDenyAll() {
        AppConfigAuthority authority = new AppConfigAuthority();
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isDenyAll() {
        return this.denyAll;
    }

    @Override
    public String getAttribute() {
        return this.denyAll ? ATTRIBUTE_DENY_ALL : (this.type + SEPARATOR + this.rank + SEPARATOR + this.app + SEPARATOR
                + this.permission);
    }

    @Override
    public String toString() {
        return getAttribute();
    }

}
