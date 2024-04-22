package org.panda.business.admin.modules.services.api.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 修改账户信息参数
 *
 * @author fangen
 **/
@Setter
@Getter
public class UpdateAuthAccountParam {
    /**
     * 账户唯一ID
     */
    @NotNull
    private Integer id;
    /**
     * 启用
     */
    private Boolean enabled;
    /**
     * 邮箱
     */
    private String email;
}
