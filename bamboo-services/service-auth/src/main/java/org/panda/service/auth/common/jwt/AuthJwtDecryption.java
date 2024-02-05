package org.panda.service.auth.common.jwt;

import org.panda.tech.core.webmvc.jwt.symmetric.JwtSymmetricDecryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Auth JWT对称解密器
 *
 * @author fangen
 **/
@Component
public class AuthJwtDecryption extends JwtSymmetricDecryption {

    @Autowired
    private AuthJwtProperties authJwtProperties;

    @Override
    public String getEncryptionName() {
        return authJwtProperties.getEncryptionName();
    }

    @Override
    protected long getStaticKey() {
        return AuthJwtProperties.STATIC_KEY;
    }

}
