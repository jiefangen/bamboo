package org.panda.modules.system.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 角色表映射对象
 *
 * @author jvfagan
 * @since JDK 1.8  2020/5/5
 **/
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String dataScope = "本级";

    private Integer level = 3;

    private String remark;

    private String permission;

    private Set<User> users;

    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
