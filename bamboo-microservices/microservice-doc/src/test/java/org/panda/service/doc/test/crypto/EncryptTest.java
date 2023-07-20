package org.panda.service.doc.test.crypto;

import org.junit.jupiter.api.Test;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.crypto.md5.Md5Encryptor;
import org.panda.tech.core.crypto.sha.ShaEncryptor;

public class EncryptTest {

    @Test
    void md5EncryptorTest() {
        Md5Encryptor encryptor = new Md5Encryptor();
        System.out.println(encryptor.encrypt("Hello World!"));
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
