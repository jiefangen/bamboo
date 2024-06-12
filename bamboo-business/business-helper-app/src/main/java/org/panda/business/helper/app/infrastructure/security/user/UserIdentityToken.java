package org.panda.business.helper.app.infrastructure.security.user;

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
    private String password;
    /**
     * 用户等级，定制权限管控
     */
    private Integer userRank;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserRank() {
        return userRank;
    }

    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
    }
}
