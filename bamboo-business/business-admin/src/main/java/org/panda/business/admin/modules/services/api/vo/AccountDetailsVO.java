package org.panda.business.admin.modules.services.api.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountDetailsVO {

    private String username;

    private String password;

    private String merchantNum;

    private String secretKey;

    private String credentials;
}
