package org.panda.service.auth.model.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class AddAccountParam {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String accountType;

    private String email;
}
