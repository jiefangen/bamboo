package org.panda.bamboo.authorization.vo;

import java.util.Date;

/**
 * 客户端标识
 *
 * @author jvfagan
 * @date: 2019-03-24
 **/
public class AuthClientInfo {
    /**
     * 客户端唯一标识，必须
     */
    private String clientId;
    /**
     * 客户端标识对应的秘钥，必须
     */
    private String clientSecret;
    /**
     * 客户端的主机名地址，可选
     */
    private String clientHost;
    /**
     * 拥有此客户端标识的对象的注册时间，可选
     */
    private Date registeredDate;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }
}
