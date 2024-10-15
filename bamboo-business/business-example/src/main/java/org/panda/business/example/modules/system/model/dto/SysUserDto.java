package org.panda.business.example.modules.system.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.example.data.entity.SysRole;
import org.panda.business.example.data.entity.SysUser;
import org.panda.tech.data.model.entity.unity.Unity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class SysUserDto implements Unity<String>, Serializable {
    private static final long serialVersionUID = -8238402102153367472L;

    private Integer userId;
    private SysUser user;
    private List<SysRole> roles;
    private Set<String> roleCodes = new HashSet<>();

    @Override
    public String getId() {
        return this.user.getUsername();
    }
}
