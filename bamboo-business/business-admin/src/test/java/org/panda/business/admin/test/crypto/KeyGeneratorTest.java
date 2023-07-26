package org.panda.business.admin.test.crypto;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.business.admin.test.AdminApplicationTest;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 固定密钥生成
 *
 * @author fangen
 **/
public class KeyGeneratorTest extends AdminApplicationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    @Test
    void fixedKeyGenerator() {
        String key = StringUtil.randomNormalMixeds(11);
        System.out.println("key: " + key);
        AesEncryptor aesEncryptor = new AesEncryptor();
        String encryptedText = aesEncryptor.encrypt(appName, key);
        System.out.println("encryptedText: " + encryptedText);
        String decryptSource = aesEncryptor.decrypt(encryptedText, key);
        System.out.println("decryptSource: " + decryptSource);
    }

    @Test
    void encode() {
        String encodedPassword = passwordEncoder.encode("123456");
        System.out.println("encodedPassword: " + encodedPassword);
    }

}
