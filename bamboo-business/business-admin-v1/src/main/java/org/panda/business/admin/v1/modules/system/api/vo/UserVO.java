package org.panda.business.admin.v1.modules.system.api.vo;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.v1.modules.system.service.entity.SysRole;
import org.panda.business.admin.v1.modules.system.service.entity.SysUser;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class UserVO implements Serializable {

    private static final long serialVersionUID = -1905046718519934587L;

    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 用户信息
     */
    private SysUser user;
    /**
     * 拥有的角色集
     */
    private List<SysRole> roles;
    /**
     * 角色权限code集
     */
    private Set<String> roleCodes = new HashSet<>();

    /**
     * 路由菜单信息
     */
    List<MenuVO> routes;

}
