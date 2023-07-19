package org.panda.business.admin.modules.system.api.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改密码参数
 *
 * @author fangen
 **/
@Setter
@Getter
public class UpdatePassParam {
    /**
     * 用户唯一ID
     */
    @NotNull
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 旧密码
     */
    @NotBlank
    private String oldPassword;
    /**
     * 新密码
     */
    @NotBlank
    private String newPassword;
}
