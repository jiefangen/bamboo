package org.panda.business.helper.app.infrastructure.security;

/**
 * 用户凭证交互token
 *
 * @author fangen
 * @since JDK 11 2024/6/11
 **/
public class UserIdentityToken {
    /**
     * 用户唯一ID
     */
    private Integer userId;
    /**
     * 用户身份标识（一般指用户名）
     */
    private String identity;
    /**
     * 凭证密码
     */
    private String credentials;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

}
