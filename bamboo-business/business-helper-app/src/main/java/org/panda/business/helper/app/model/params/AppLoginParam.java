package org.panda.business.helper.app.model.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录参数
 *
 * @author fangen
 * @since 2024/6/6
 **/
@Data
public class AppLoginParam {
    /**
     * 用户名
     */
    @NotBlank
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 微信openid
     */
    private String openid;
    /**
     * 头像
     */
    private String avatar;
}
