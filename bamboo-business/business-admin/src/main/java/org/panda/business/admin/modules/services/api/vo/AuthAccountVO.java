package org.panda.business.admin.modules.services.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 认证账户视图
 *
 * @author fangen
 **/
@Data
public class AuthAccountVO implements Serializable {
    private static final long serialVersionUID = 2221651646426400543L;

    private Integer id;

    private String username;

    private String secretKey;

    private String credentials;

    private String merchantNum;

    private String accountType;

    private String accountRank;

    private String email;

    private Boolean enabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
