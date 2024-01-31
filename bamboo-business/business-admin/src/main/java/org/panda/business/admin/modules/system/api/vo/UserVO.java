package org.panda.business.admin.modules.system.api.vo;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.system.service.entity.SysRole;
import org.panda.business.admin.modules.system.service.entity.SysUser;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class UserVO extends SysUser implements Serializable {

    private static final long serialVersionUID = 759457472720480753L;

    /**
     * 拥有的角色集
     */
    private List<SysRole> roles;
    /**
     * 角色权限code集
     */
    private Set<String> roleCodes;
    /**
     * 角色权限集-code|roleId
     */
    private Set<String> roleCodeAndIds;
    /**
     * 路由菜单信息
     */
    private List<MenuVO> routes;

    public UserVO(SysUser sysUser){
        this.transform(sysUser);
    }

    public void transform(SysUser sysUser) {
        this.setId(sysUser.getId());
        this.setUsername(sysUser.getUsername());
        this.setUserType(sysUser.getUserType());
        this.setUserRank(sysUser.getUserRank());
        this.setNickname(sysUser.getNickname());
        this.setSex(sysUser.getSex());
        this.setPhone(sysUser.getPhone());
        this.setEmail(sysUser.getEmail());
        this.setEnabled(sysUser.getEnabled());
        this.setCreateTime(sysUser.getCreateTime());
        this.setUpdateTime(sysUser.getUpdateTime());
    }

}
