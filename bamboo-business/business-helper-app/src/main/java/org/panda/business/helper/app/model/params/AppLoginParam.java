package org.panda.business.helper.app.model.params;

import lombok.Data;
import org.panda.support.openapi.model.EncryptedData;

/**
 * 用户登录参数
 *
 * @author fangen
 * @since 2024/6/6
 **/
@Data
public class AppLoginParam {
    /**
     * 登录方式：account-账户登录；sms-短信登录
     * 为空默认微信小程序登录方式
     */
    private String loginMode;

/* ---------- 通用用户名密码登录参数 ---------- */
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

/* ---------- 微信小程序登录专用参数 ---------- */
    /**
     * 微信临时凭证code
     */
    private String code;
    /**
     * 微信用户唯一凭证
     */
    private String openid;
    /**
     * 加密数据
     */
    private EncryptedData encryptedData;
}
