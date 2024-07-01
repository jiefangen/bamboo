package org.panda.business.helper.app.infrastructure.security.user;

/**
 * 微信用户token
 *
 * @author fangen
 * @since JDK 11 2024/6/30
 **/
public class WechatUserToken extends UserIdentityToken {
    /**
     * 微信用户唯一标识
     */
    private String openid;
    /**
     * 应用appid
     */
    private String appid;
}
