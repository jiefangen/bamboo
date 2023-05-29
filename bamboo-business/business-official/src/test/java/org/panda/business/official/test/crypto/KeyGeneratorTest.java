package org.panda.business.official.test.crypto;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.lang.MathUtil;
import org.panda.tech.core.crypto.aes.AesEncryptor;

/**
 * 固定密钥生成
 *
 * @author fangen
 **/
public class KeyGeneratorTest {

    @Test
    void fixedKeyGenerator() {
        String source = "business-official";
        String key = Long.toString(MathUtil.randomLong(10000000, 99999999));
        System.out.println("key: " + key);
        AesEncryptor aesEncryptor = new AesEncryptor();
        String encryptedText = aesEncryptor.encrypt(source, key);
        System.out.println("encryptedText: " + encryptedText);
        String decryptSource = aesEncryptor.decrypt(encryptedText, key);
        System.out.println("decryptSource: " + decryptSource);
    }

}
