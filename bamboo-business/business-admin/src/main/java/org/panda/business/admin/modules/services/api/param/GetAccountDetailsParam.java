package org.panda.business.admin.modules.services.api.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class GetAccountDetailsParam {

    private String username;

    private String password;

    private String merchantNum;

    @NotEmpty
    private String secretKey;

    @NotEmpty
    private String credentials;
}
