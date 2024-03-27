package org.panda.tech.core.jwt.encrypt.symmetric;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.crypto.md5.Md5xEncryptor;

/**
 * JWT对称密钥支持
 */
public abstract class JwtSymmetricSecretKeySupport {

    private final Md5xEncryptor encryptor = new Md5xEncryptor(getStaticKey());
    private String defaultSecretKey; // 对称密钥大概率不区分业务类型，缓存默认密钥以提高密钥获取速度

    public abstract String getEncryptionName();
    
    public boolean isSymmetric(String type) {
        return true;
    }

    protected final String getSecretKey(String type) {
        if (StringUtils.isBlank(type)) {
            if (this.defaultSecretKey == null) {
                this.defaultSecretKey = this.encryptor.encrypt(Strings.EMPTY, getEncryptionName());
            }
            return this.defaultSecretKey;
        }
        return this.encryptor.encrypt(type, getEncryptionName());
    }

    protected abstract long getStaticKey();

}
