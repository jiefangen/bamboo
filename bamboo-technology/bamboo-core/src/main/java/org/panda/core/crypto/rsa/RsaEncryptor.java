package org.panda.core.crypto.rsa;

import org.panda.core.crypto.KeyBilateralEncryptor;
import org.panda.core.util.EncryptUtil;

/**
 * RSA算法加密器（非对称可逆）
 * 算法简介：每个用户都拥有一对公钥和私钥。公钥可以自由传播，用于加密数据，而私钥只能由用户自己持有，用于解密数据。
 * 使用场景：保护通信数据、数字签名、身份认证等。
 *
 * @author fangen
 */
public class RsaEncryptor implements KeyBilateralEncryptor {

    @Override
    public String encrypt(Object source, Object key) {
        return EncryptUtil.encryptByRsa(source, key);
    }

    @Override
    public String decrypt(String encryptedText, Object key) {
        return EncryptUtil.decryptByRsa(encryptedText, key);
    }

}
