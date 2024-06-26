package org.panda.business.helper.app.model.params;

import lombok.Data;

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
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户昵称
     */
    private String nickname;

/* ----------微信小程序登录所需参数---------- */
    /**
     * AppId
     */
    private String appid;
    /**
     * 微信临时凭证code
     */
    private String code;
}
