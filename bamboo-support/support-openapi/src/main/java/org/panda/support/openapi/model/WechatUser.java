package org.panda.support.openapi.model;

/**
 * 微信用户标识
 *
 * @author fangen
 */
public class WechatUser {

    private WechatAppType appType;
    private String openid;
    private String unionId;
    private String sessionKey;
    private String accessToken;

    public WechatAppType getAppType() {
        return this.appType;
    }

    public void setAppType(WechatAppType appType) {
        this.appType = appType;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getSessionKey() {
        return this.sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
