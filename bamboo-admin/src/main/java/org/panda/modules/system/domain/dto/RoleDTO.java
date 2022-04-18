package org.panda.modules.system.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.panda.modules.system.domain.po.MenuPO;
import org.panda.modules.system.domain.po.RolePO;

import java.util.List;

@Setter
@Getter
public class RoleDTO extends RolePO {
    private List<MenuPO> routes;

    public RoleDTO(){}

    public RoleDTO(RolePO role){
        this.transform(role);
    }

    public void transform(RolePO role){
        this.setId(role.getId());
        this.setRoleName(role.getRoleName());
        this.setRoleCode(role.getRoleCode());
        this.setDescription(role.getDescription());
        this.setCreateTime(role.getCreateTime());
        this.setUpdateTime(role.getUpdateTime());
    }
}
