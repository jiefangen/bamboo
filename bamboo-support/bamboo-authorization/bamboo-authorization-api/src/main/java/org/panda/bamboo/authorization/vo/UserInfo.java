package org.panda.bamboo.authorization.vo;

/**
 * 用户账号
 *
 * @author jvfagan
 * @date: 2019-03-24
 **/
public class UserInfo {
    /**
     * 账号信息标识号，可选
     */
    private int userId;
    /**
     * 登录用户名，必须
     */
    private String userName;
    /**
     * 登录密码，必须
     */
    private String password;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
