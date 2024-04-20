package org.panda.business.admin.modules.services.api.param;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddAccountParam {

    private String username;

    private String password;

    private String accountType;

    private String email;
}
