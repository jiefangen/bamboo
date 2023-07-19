package org.panda.business.admin.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.system.api.vo.MenuVO;
import org.panda.business.admin.modules.system.service.entity.SysRole;

import java.util.List;

@Setter
@Getter
public class SysRoleDto extends SysRole {
    private List<MenuVO> routes;

    public SysRoleDto(){}

    public SysRoleDto(SysRole role){
        this.transform(role);
    }

    public void transform(SysRole role){
        this.setId(role.getId());
        this.setRoleName(role.getRoleName());
        this.setRoleCode(role.getRoleCode());
        this.setDescription(role.getDescription());
        this.setCreateTime(role.getCreateTime());
        this.setUpdateTime(role.getUpdateTime());
    }
}
