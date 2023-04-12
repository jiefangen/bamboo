package org.panda.tech.core.crypto.base64;

import org.panda.bamboo.common.util.EncryptUtil;
import org.panda.tech.core.crypto.BilateralEncryptor;

/**
 * BASE6编码算法（可逆）
 * 算法简介：Base64编码使用64个字符（A-Z，a-z，0-9，+和/）来表示二进制数据，其中每个字符表示6个位。
 * 使用场景：电子邮件中传输二进制文件、Web应用程序中传输数据、存储密码等。
 *
 * @author fangen
 */
public class Base64Encryptor implements BilateralEncryptor {

    public static Base64Encryptor INSTANCE = new Base64Encryptor();

    private Base64Encryptor() {
    }

    @Override
    public String encrypt(Object source) {
        return EncryptUtil.encryptByBase64(source);
    }

    @Override
    public String decrypt(String encryptedText) {
        return EncryptUtil.decryptByBase64(encryptedText);
    }

}
