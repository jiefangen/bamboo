package org.panda.business.official.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.official.modules.system.service.entity.SysRole;
import org.panda.business.official.modules.system.service.entity.SysUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class SysUserDto extends SysUser {

    private List<SysRole> roles;
    private Set<String> roleCodes = new HashSet<>();

    public SysUserDto(SysUser sysUser){
        this.transform(sysUser);
    }

    public void transform(SysUser sysUser){
        this.setId(sysUser.getId());
        this.setUsername(sysUser.getUsername());
        this.setPassword(sysUser.getPassword());
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
