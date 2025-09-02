package org.panda.business.example.test.algorithm;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.crypto.md5.Md5Encryptor;
import org.panda.tech.core.crypto.sha.ShaEncryptor;

/**
 * 加密测试类
 *
 * @author fangen
 * @since 2025/9/2
 **/
public class EncryptTest {

    @Test
    void generateKey() {
        String key = StringUtil.randomNormalMixeds(16);
        System.out.println(key);
    }

    @Test
    void md5EncryptorTest() {
        String key = StringUtil.randomNormalMixeds(16);
        System.out.println(key);
        Md5Encryptor encryptor = new Md5Encryptor();
        System.out.println(encryptor.encrypt(key));
        String plaintext = "jfg123456/";
        System.out.println("明文：" + plaintext + "《================》秘文：" + encryptor.encrypt(plaintext));
    }

    @Test
    void shaEncryptorTest() {
        ShaEncryptor encryptor = new ShaEncryptor();
        System.out.println(encryptor.encrypt("admin-vue"));
    }

    @Test
    void aesEncryptorTest() {
        AesEncryptor encryptor = new AesEncryptor();
        String source = "Hello World!";
        String key = "ajikouehnfkolkjh";
        String encryptSource = encryptor.encrypt(source, key);
        System.out.println(encryptSource);
        System.out.println("-----------------------------");
        System.out.println(encryptor.decrypt(encryptSource, key));
    }
}
