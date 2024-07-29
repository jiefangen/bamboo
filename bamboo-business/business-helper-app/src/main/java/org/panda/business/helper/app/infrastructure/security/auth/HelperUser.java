package org.panda.business.helper.app.infrastructure.security.auth;

import org.panda.business.helper.app.model.vo.UserInfo;
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
     * 用户信息
     */
    private UserInfo userInfo;

    public HelperUser() {
        super.setType(DEFAULT_TYPE);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
