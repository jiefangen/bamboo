package org.panda.tech.core.crypto;

/**
 * 使用密钥的解密器
 *
 * @author fangen
 */
public interface KeyDecryptor {

    String decrypt(String encryptedText, Object key);

}
