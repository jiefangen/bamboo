package org.panda.business.admin.modules.services.api.param;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddAccountParam {

    private String username;

    /**
     * 账户原密码
     */
    private String password;
    /**
     * 加密后的密码
     */
    private String encodedPassword;

    private String accountType;

    private String email;
}
