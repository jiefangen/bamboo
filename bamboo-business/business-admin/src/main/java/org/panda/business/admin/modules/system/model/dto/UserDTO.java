package org.panda.business.admin.modules.system.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.system.model.po.RolePO;
import org.panda.business.admin.modules.system.model.po.UserPO;
import org.panda.business.admin.modules.system.model.vo.MenuVO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class UserDTO extends UserPO {
    private List<RolePO> roles;

    private Set<String> roleCodes = new HashSet<>();

    List<MenuVO> routes;

    public UserDTO(){}

    public UserDTO(UserPO userPO){
        this.transform(userPO);
    }

    public void transform(UserPO userPO){
        this.setId(userPO.getId());
        this.setUsername(userPO.getUsername());
        this.setPassword(userPO.getPassword());
        this.setSalt(userPO.getSalt());
        this.setNickname(userPO.getNickname());
        this.setSex(userPO.getSex());
        this.setPhone(userPO.getPhone());
        this.setEmail(userPO.getEmail());
        this.setDisabled(userPO.getDisabled());
        this.setCreateTime(userPO.getCreateTime());
        this.setUpdateTime(userPO.getUpdateTime());
    }
}