package org.panda.service.auth.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.panda.service.auth.model.entity.AuthAccount;
import org.panda.service.auth.model.entity.AuthRole;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class AuthAccountDto implements Serializable {

    private static final long serialVersionUID = 5509979819382067155L;

    private Integer accountId;
    private AuthAccount account;
    private List<AuthRole> roles;
    private Set<String> roleCodes = new HashSet<>();

}
