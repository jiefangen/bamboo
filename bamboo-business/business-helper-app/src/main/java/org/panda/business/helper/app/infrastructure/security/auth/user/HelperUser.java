package org.panda.business.helper.app.infrastructure.security.auth.user;

import org.panda.tech.core.spec.user.DefaultUserIdentity;

/**
 * 助手用户唯一标识
 *
 * @author fangen
 * @since JDK 11 2024/7/17
 **/
public class HelperUser extends DefaultUserIdentity {

    private static final long serialVersionUID = -6868678754213722814L;

    private static final String DEFAULT_TYPE = "helper";
    /**
     * 用户唯一ID
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户等级
     */
    private Integer userRank;

    public HelperUser() {
        super.setType(DEFAULT_TYPE);
        super.setId(this.userId);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserRank() {
        return userRank;
    }

    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
    }
}
