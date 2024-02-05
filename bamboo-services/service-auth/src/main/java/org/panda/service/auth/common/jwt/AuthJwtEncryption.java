package org.panda.service.auth.common.jwt;

import org.apache.commons.lang3.StringUtils;
import org.panda.tech.core.webmvc.jwt.symmetric.JwtSymmetricEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Auth JWT对称加密器
 *
 * @author fangen
 **/
@Component
public class AuthJwtEncryption extends JwtSymmetricEncryption {

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

    @Override
    public String getPayload(String type) {
        if (StringUtils.isNotBlank(type)) {
            // 通过type生成访问应用相关信息
        }
        return AuthJwtProperties.DEFAULT_PAYLOAD;
    }
}
