package org.panda.business.admin.modules.system.api.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 重置密码参数
 *
 * @author fangen
 **/
@Setter
@Getter
public class ResetPassParam {
    /**
     * 用户ID
     */
    @NotNull
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 新密码
     */
    private String newPassword;
}
