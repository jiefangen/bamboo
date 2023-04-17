package org.panda.business.admin.modules.system.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.system.domain.po.RolePO;
import org.panda.business.admin.modules.system.domain.vo.MenuVO;

import java.util.List;

@Setter
@Getter
public class RoleDTO extends RolePO {
    private List<MenuVO> routes;

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
