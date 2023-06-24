package org.panda.business.admin.v1.modules.system.api.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 重置密码参数
 *
 * @author fangen
 **/
@Setter
@Getter
public class ResetPassParam {
    /**
     * 用户名
     */
    private String username;
    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;
}
