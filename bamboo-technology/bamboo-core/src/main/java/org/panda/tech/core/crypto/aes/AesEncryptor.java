package org.panda.tech.core.crypto.aes;

import org.panda.bamboo.common.util.EncryptUtil;
import org.panda.tech.core.crypto.KeyBilateralEncryptor;

/**
 * AES算法加密器（对称可逆）
 * 算法简介：采用固定长度的密钥，密钥长度分别为128bit（16ASCII字符）、192bit（24ASCII字符）和256bit（32ASCII字符），
 * 根据不同的密钥长度分别采用10轮、12轮和14轮加密计算。
 * 使用场景：网络传输的数据加密、存储数据的加密、数据库加密、文件加密等。
 *
 * @author fangen
 */
public class AesEncryptor implements KeyBilateralEncryptor {

    @Override
    public String encrypt(Object source, Object key) {
        return EncryptUtil.encryptByAes((String) source, (String) key);
    }

    @Override
    public String decrypt(String encryptedText, Object key) {
        return EncryptUtil.decryptByAes(encryptedText, (String) key);
    }

}
