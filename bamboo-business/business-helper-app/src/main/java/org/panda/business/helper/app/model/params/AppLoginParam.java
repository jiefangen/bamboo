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
    /**
     * 应用的AppId
     */
    private String appid;

/* ----------微信小程序登录所需参数---------- */
    /**
     * 微信用户唯一凭证
     */
    private String openid;
    /**
     * 微信临时凭证code
     */
    private String code;
    /**
     * 包括敏感数据在内的完整用户信息的加密数据
     */
    private String encryptedData;
    /**
     * 加密算法的初始向量
     */
    private String iv;
}
