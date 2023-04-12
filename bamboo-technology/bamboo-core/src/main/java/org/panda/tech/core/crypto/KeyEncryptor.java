package org.panda.tech.core.crypto;

/**
 * 使用密钥的加密器
 *
 * @author fangen
 */
public interface KeyEncryptor {

    String encrypt(Object source, Object key);

}
