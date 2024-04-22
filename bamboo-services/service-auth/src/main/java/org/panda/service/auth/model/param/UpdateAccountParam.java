package org.panda.service.auth.model.param;

import lombok.Getter;
import lombok.Setter;

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
