package org.panda.modules.system.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.panda.modules.system.domain.po.RolePO;
import org.panda.modules.system.domain.po.UserPO;

import java.util.List;

@Setter
@Getter
public class UserDTO extends UserPO {
    private List<RolePO> roles;

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
