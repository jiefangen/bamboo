package org.panda.tech.core.jwt.encrypt.symmetric;

import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.jwt.encrypt.JwtEncryption;
import org.springframework.beans.factory.annotation.Value;

/**
 * JWT对称加密方
 */
public abstract class JwtSymmetricEncryption extends JwtSymmetricSecretKeySupport implements JwtEncryption {

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String name;

    @Override
    public String getEncryptionName() {
        return this.name;
    }

    @Override
    public final boolean isSymmetric(String type) {
        return true;
    }

    @Override
    public final String getEncryptSecretKey(String type) {
        return getSecretKey(type);
    }

}
