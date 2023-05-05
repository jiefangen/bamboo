package org.panda.tech.security.authentication;

/**
 * 短信验证码认证令牌
 */
public class SmsVerifyCodeAuthenticationToken extends UnauthenticatedAuthenticationToken {

    private static final long serialVersionUID = -1206641448564918380L;

    public SmsVerifyCodeAuthenticationToken(String cellphone, String verifyCode) {
        super(cellphone, verifyCode);
    }

    public String getCellphone() {
        return (String) getPrincipal();
    }

    public String getVerifyCode() {
        return (String) getCredentials();
    }

}
