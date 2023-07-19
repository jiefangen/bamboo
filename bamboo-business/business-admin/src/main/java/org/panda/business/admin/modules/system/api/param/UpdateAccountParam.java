package org.panda.business.admin.modules.system.api.param;

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
public class UpdateAccountParam {
    /**
     * 用户唯一ID
     */
    @NotNull
    private Integer id;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private String sex;
}
