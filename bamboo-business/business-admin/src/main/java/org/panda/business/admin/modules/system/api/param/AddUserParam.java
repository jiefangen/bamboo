package org.panda.business.admin.modules.system.api.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 新增用户参数
 *
 * @author fangen
 **/
@Setter
@Getter
public class AddUserParam {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String phone;

    private String nickname;

    private String userType;

    private String email;

    private String sex;
}
