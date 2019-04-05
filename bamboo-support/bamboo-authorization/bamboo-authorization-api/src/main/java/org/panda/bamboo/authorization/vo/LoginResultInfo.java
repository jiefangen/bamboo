package org.panda.bamboo.authorization.vo;

/**
 * 登录返回结果
 *
 * @author jvfagan
 * @date: 2019-03-24
 **/
public class LoginResultInfo {
    /**
     * 认证服务器返回用户标识号，必须
     */
    private String userId;
    /**
     * 认证服务器返回登录用户名，如果认证失败此项为空，必须
     */
    private String userName;
    /**
     * 授权服务器返回该用户拥有的角色名称，可选
     */
    private String roles;

    /**
     * 登录错误信息，可选
     */
    private String errorMessage;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
